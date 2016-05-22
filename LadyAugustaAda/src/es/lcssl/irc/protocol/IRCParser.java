/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 19 de may. de 2016, 18:00:40
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents the IRC parser that gets bytes from the {@link InputStream}
 * configured within and constructs the proper kind of messages to deliver them to the
 * configured observers.
 * 
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 *
 */
public class IRCParser {
	
	private InputStream   m_in;
	private Status        m_st;
	
	private StringBuilder m_orgStr;
	private StringBuilder m_codStr;
	private StringBuilder m_parStr;
	
	private Origin        m_origin;
	private IRCCode   m_msgCode;
	private IRCMessage  m_message;
	
	private enum Status {
		/**
		 * This state is the initial state and the state when
		 * no part of a message has been read.  It is also the
		 * state reached after error recovery.
		 */
		INITIAL, 
		
		/**
		 * This state gets entered when an initial ':' character
		 * has arrived as the first of a message.  It signals an
		 * {@link Origin} field is present and is maintained while
		 * filling the origin address of this message.
		 */
		ORIGIN, 
		
		/**
		 * This state is maintained while reading the blank space that
		 * exist between the {@link Origin} field and the command of
		 * a message.
		 */
		PRE_CODE, 
		
		/**
		 * This state is maintained while reading the command part of
		 * a message.  Also when a response code is present for a response
		 * message.
		 */
		INCODE, 
		
		/**
		 * This state is maintained while reading the blank space present
		 * between command and middle parameters, or the blank space present
		 * between middle parameters/command and final parameter.
		 */
		INTER_PARM, 
		
		/**
		 * This state is held while reading the nonblank part of a middle
		 * parameter.
		 */
		MIDDLE, 
		
		/**
		 * This state is held while reading the final parameter.  Final
		 * parameter is only delimited by the presence of a `\r` or a `\n`
		 * character.
		 */
		FINAL, 
		
		/**
		 * This state marks the end of the IRC message and only a new line
		 * '\n' character is allowed.
		 */
		IN_CR, 
		
		/**
		 * This state is held when encountering a parsing error and is maintained
		 * until a '\n' is received in the input stream.
		 */
		ERROR,
	}
	
	public IRCParser(InputStream in) {
		m_in = in;
		reset();
	}	
	
	public void reset() {
		m_st      = Status.INITIAL;
		
		m_orgStr  = null;
		m_codStr  = null;
		m_codStr  = null;
		m_parStr  = null;
		
		m_origin  = null;
		m_msgCode = null;
		m_message = null;
	}

	private void createTheMessage(Status nextSt) {
		m_msgCode = IRCCode.fromProto(m_codStr.toString());
		if (m_msgCode == null) { // not valid message code.
			m_st = Status.ERROR;
			System.err.println("Error: invalid msgcode: " + m_codStr);
		} else {
			m_st = nextSt;
			m_message = new IRCMessage(m_origin, m_msgCode);
		}
	}

	public IRCMessage scan() {
		
		int c;
		
		// just to ensure we are clean.
		if (m_st == Status.INITIAL) reset();

		try {
			while ((c = m_in.read()) != -1) {
				char ch = (char) c;

				switch (m_st) {

				case INITIAL:
					switch (ch) {
					case ':': 
						m_st = Status.ORIGIN; 
						m_orgStr = new StringBuilder(); 
						continue;
					case ' ': case '\t': 
					case '\n': 
						continue;
					case '\r': 
						m_st = Status.IN_CR; 
						continue;
					default:
						m_st = Status.INCODE;
						m_codStr = new StringBuilder();
						m_codStr.append(ch);
						continue;
					} 

				case ORIGIN:
					switch (ch) {
					case '\t': case ' ': 
						m_st = Status.PRE_CODE;
						m_origin = new Origin(m_orgStr.toString()); m_orgStr = null;
						continue;
					case '\r': 
						m_st = Status.IN_CR; 
						continue;
					case '\n': 
						reset(); 
						continue;
					default: 
						m_orgStr.append(ch); 
						continue;
					}

				case PRE_CODE:
					switch (ch) {
					case ' ': case '\t': 
						continue;
					case '\r': 
						m_st = Status.IN_CR; 
						continue;
					case '\n': 
						reset(); 
						continue;
					default:
						m_st = Status.INCODE;
						m_codStr = new StringBuilder();
						m_codStr.append(ch);
						continue;
					}

				case INCODE:
					switch (ch) {
					case ' ': case '\t':
						createTheMessage(Status.INTER_PARM);
						continue;
					case '\r':
						createTheMessage(Status.IN_CR);
						continue;
					case '\n':
						createTheMessage(Status.INITIAL);
						return m_message;
					default:
						m_codStr.append(ch);
						continue;
					}

				case INTER_PARM:
					switch (ch) {
					case ' ': case '\t':
						continue;
					case '\r':
						m_st = Status.IN_CR;
						continue;
					case '\n':
						m_st = Status.INITIAL;
						return m_message;
					case ':':
						m_st = Status.FINAL;
						m_parStr = new StringBuilder();
						continue;
					default:
						m_st = Status.MIDDLE;
						m_parStr = new StringBuilder();
						m_parStr.append(ch);
						continue;
					}

				case MIDDLE:
					switch (ch) {
					case ' ': case '\t':
						m_st = Status.INTER_PARM;
						m_message.getParams().add(m_parStr.toString());
						m_parStr = null;
						continue;
					case '\r':
						m_st = Status.IN_CR;
						m_message.getParams().add(m_parStr.toString());
						m_parStr = null;
						continue;
					case '\n':
						m_st = Status.INITIAL;
						return m_message;
					default:
						m_parStr.append(ch);
						continue;
					}

				case FINAL:
					switch (ch) {
					case '\r':
						m_st = Status.IN_CR;
						m_message.getParams().add(m_parStr.toString());
						continue;
					case '\n':
						m_st = Status.INITIAL;
						m_message.getParams().add(m_parStr.toString());
						return m_message;
					default:
						m_parStr.append(ch);
						continue;
					} 

				case IN_CR:
					switch (ch) {
					case '\n':
						m_st = Status.INITIAL;
						return m_message;
					default:
						m_st = Status.ERROR;
						System.err.println("Syntax error: some chars between \\r and \\n");
						continue;
					}
				case ERROR:
					switch (ch) {
					case '\n': 
						m_st = Status.INITIAL; 
						continue;
					default: 
						continue;
					}
				} // switch
			} // while
		} catch (IOException e) {
			e.printStackTrace();
		} // while
		return null;
	} // scan()
}
