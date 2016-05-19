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
import java.util.Iterator;

/**
 * This class represents the IRC parser that gets bytes from the {@link InputStream}
 * configured within and constructs the proper kind of messages to deliver them to the
 * configured observers.
 * 
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 *
 */
public class IRCParser implements Iterable<IRCMessage>, Iterator<IRCMessage>, Runnable {
	
	private InputStream m_in;
	private Status m_st;
	private StringBuilder m_origStr;
	private Origin m_origin;
	private StringBuilder m_reqStr;
	private RequestCode m_reqCode;
	private StringBuilder m_respStr;
	private ResponseCode m_respCode;
	private StringBuilder m_param;
	private Request m_request;
	private Response m_response;
	
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
		PRE_COMMAND, 
		
		/**
		 * This state is maintained while reading the command part of
		 * a message.  Also when a response code is present for a response
		 * message.
		 */
		COMMAND, 
		
		/**
		 * This state is held while reading a response code.
		 */
		RESPONSE,
		
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
	}
	
	private void init() {
		m_st = Status.INITIAL;
		m_origStr = null;
		m_origin = null;
		m_reqStr = null;
		m_reqCode = null;
		m_respStr = null;
		m_respCode = null;
		m_param = null;
		m_request = null;
		m_response = null;
	}
	
	private void trigger() {
		
	}
	
	@Override
	public boolean hasNext() {
		
		if (m_request != null) 
			return true;

		int c;
		init();
		
		try {
			while ((c = m_in.read()) != -1) {
				char ch = (char) c;

				switch (m_st) {

				case INITIAL:
					switch (ch) {
					case ':': m_st = Status.ORIGIN; 
						m_origStr = new StringBuilder(); 
						continue;
					case ' ': case '\t': continue;
					case '\r': m_st = Status.IN_CR; continue;
					case '\n': init(); continue;
					case '0': case '1': case '2': case '3': case '4':
					case '5': case '6': case '7': case '8': case '9':
						m_st = Status.RESPONSE;
						m_respStr = new StringBuilder(ch);
						continue;
					default:
						m_st = Status.COMMAND;
						m_reqStr = new StringBuilder(ch);
						continue;
					} 

				case ORIGIN:
					switch (c) {
					case '\t': case ' ': 
						m_st = Status.PRE_COMMAND;
						m_origin = new Origin(m_origStr.toString());
						m_reqStr = new StringBuilder();
						continue;
					case '\r': m_st = Status.IN_CR; continue;
					case '\n': init(); continue;
					default: m_origStr.append((char)c); continue;
					}

				case PRE_COMMAND:
					switch (c) {
					case ' ': case '\t': continue;
					case '\r': m_st = Status.IN_CR; continue;
					case '\n': init(); continue;
					case '0': case '1': case '2': case '3': case '4':
					case '5': case '6': case '7': case '8': case '9':
						m_st = Status.RESPONSE;
						m_respStr = new StringBuilder(ch);
						continue;
					default:
						m_st = Status.COMMAND;
						m_reqStr = new StringBuilder(ch);
						continue;
					}

				case COMMAND:
					switch (c) {
					case ' ': case '\t':
						m_reqCode = RequestCode.valueOf(m_reqStr.toString());
						if (m_reqCode == null) {
							m_st = Status.ERROR;
						} else {
							
							m_request = m_origin != null 
									? new Request(m_origin, m_reqCode) 
									: new Request(m_reqCode);
							m_st = Status.INTER_PARM;
						}
						continue;
					case '\r':
						m_reqCode = RequestCode.valueOf(m_reqStr.toString());
						if (m_reqCode == null) {
							m_st = Status.ERROR;
						} else {
							
							m_request = m_origin != null
									? new Request(m_origin, m_reqCode)
									: new Request(m_reqCode);
							m_st = Status.IN_CR;
						}
					case '\n':
						m_reqCode = RequestCode.valueOf(m_reqStr.toString());
					default:
					} break;

				case RESPONSE:
					switch (c) {
					case ' ':
					case '\t':
					case '\r':
					case '\n':
					default:
					} break;

				case INTER_PARM:
					switch (c) {
					case ' ':
					case '\t':
					case '\r':
					case '\n':
					default:
					} break;

				case MIDDLE:
					switch (c) {
					case ':':
					case ' ':
					case '\t':
					case '\r':
					case '\n':
					default:
					} break;

				case FINAL:
					switch (c) {
					case ':':
					case ' ':
					case '\t':
					case '\r':
					case '\n':
					default:
					} break;

				case IN_CR:
					switch (c) {
					case ':':
					case ' ':
					case '\t':
					case '\r':
					case '\n':
					default:
					} break;
				case ERROR:
					switch (c) {
					case '\n': m_st = Status.INITIAL; continue;
					default: continue;
					}
				} // switch
			}
		} catch (IOException e) {
			e.printStackTrace();
		} // while
		return false;
	} // hasNext()

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<IRCMessage> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public IRCMessage next() {
		// TODO Auto-generated method stub
		return null;
	}
}
