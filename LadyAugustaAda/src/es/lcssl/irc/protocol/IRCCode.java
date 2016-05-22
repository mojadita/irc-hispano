/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 19 de may. de 2016, 18:20:09
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

import java.util.Map;
import java.util.TreeMap;

/**
 * Message codes available for Messages.
 * 
 * @author Luis Colorado {@code <lc@luiscoloradosistemas.com>}
 *
 */
public enum IRCCode {
	
	// requests
	PASS,
	NICK,
	USER,
	SERVER,
	OPER,
	QUIT,
	SQUIT,
	JOIN,
	PART,
	MODE,
	TOPIC,
	NAMES,
	LIST,
	INVITE,
	KICK,
	VERSION,
	STATS,
	LINKS,
	TIME,
	CONNECT,
	TRACE,
	ADMIN,
	INFO,
	PRIVMSG,
	NOTICE,
	WHO,
	WHOIS,
	WHOWAS,
	KILL,
	PING,
	PONG,
	ERROR,
	AWAY,
	REHASH,
	RESTART,
	SUMMON,
	USERS,
	WALLOPS,
	USERHOST,
	ISON,
	
	// responses
	RPL_WELCOME(1),
	RPL_YOURHOST(2),
	RPL_CREATED(3),
	RPL_MYINFO(4),
	RPL_BOUNCE(5),
	
	RPL_TRACELINK(200),
	RPL_TRACECONNECTING(201),
	RPL_TRACEHANDSHAKE(202),
	RPL_TRACEUNKNOWN(203),
	RPL_TRACEOPERATOR(204),
	RPL_TRACEUSER(205),
	RPL_TRACESERVER(206),
	RPL_TRACENEWTYPE(208),
	RPL_TRACECLASS(209),
	RPL_STATSLINKINFO(211),
	RPL_STATSCOMMANDS(212),
	RPL_STATSCLINE(213),
	RPL_STATSNLINE(214),
	RPL_STATSILINE(215),
	RPL_STATSKLINE(216),
	RPL_STATSQLINE(217),
	RPL_STATSYLINE(218),
	RPL_ENDOFSTATS(219),
	RPL_UMODEIS(221),
	RPL_SERVICEINFO(231),
	RPL_ENDOFSERVICES(232),
	RPL_SERVICE(233),
	RPL_SERVLIST(234),
	RPL_SERVLISTEND(235),
	RPL_STATSLLINE(241),
	RPL_STATSUPTIME(242),
	RPL_STATSOLINE(243),
	RPL_STATSHLINE(244),
	RPL_LUSERCLIENT(251),
	RPL_LUSEROP(252),
	RPL_LUSERUNKNOWN(253),
	RPL_LUSERCHANNELS(254),
	RPL_LUSERME(255),
	RPL_ADMINME(256),
	RPL_ADMINLOC1(257),
	RPL_ADMINLOC2(258),
	RPL_ADMINEMAIL(259),
	RPL_TRACELOG(261),
	
	RPL_AWAY(301),
	RPL_USERHOST(302),
	RPL_ISON(303),
	RPL_UNAWAY(305),
	RPL_NOWAWAY(306),
	RPL_WHOISUSER(311),
	RPL_WHOISSERVER(312),
	RPL_WHOISOPERATOR(313),
	RPL_WHOWASUSER(314),
	RPL_ENDOFWHO(315),
	RPL_WHOISCHANOP(316),
	RPL_WHOISIDLE(317),
	RPL_ENDOFWHOIS(318),
	RPL_WHOISCHANNELS(319),
	RPL_LISTSTART(321),
	RPL_LIST(322),
	RPL_LISTEND(323),
	RPL_CHANNELMODEIS(324),
	RPL_NOTOPIC(331),
	RPL_TOPIC(332),
	RPL_INVITING(341),
	RPL_SUMMONING(342),
	RPL_VERSION(351),
	RPL_WHOREPLY(352),
	RPL_NAMREPLY(353),
	RPL_KILLDONE(361),
	RPL_CLOSING(362),
	RPL_CLOSEEND(363),
	RPL_LINKS(364),
	RPL_ENDOFLINKS(365),
	RPL_ENDOFNAMES(366),
	RPL_BANLIST(367),
	RPL_ENDOFBANLIST(368),
	RPL_ENDOFWHOWAS(369),
	RPL_INFO(371),
	RPL_MOTD(372),
	RPL_INFOSTART(373),
	RPL_ENDOFINFO(374),
	RPL_MOTDSTART(375),
	RPL_ENDOFMOTD(376),
	RPL_YOUREOPER(381),
	RPL_REHASHING(382),
	RPL_MYPORTIS(384),
	RPL_TIME(391),
	RPL_USERSSTART(392),
	RPL_USERS(393),
	RPL_ENDOFUSERS(394),
	RPL_NOUSERS(395),

	// errors
	ERR_NOSUCHNICK(401),
	ERR_NOSUCHSEVER(402),
	ERR_NOSUCHCHANNEL(403),
	ERR_CANNOTSENDTOCHAN(404),
	ERR_TOOMANYCHANNELS(405),
	ERR_WASNOSUCHNICK(406),
	ERR_TOOMANYTARGETS(407),
	ERR_NOORIGIN(409),
	ERR_NORECIPIENT(411),
	ERR_NOTEXTTOSEND(412),
	ERR_NOTOPLEVEL(413),
	ERR_WILDTOPLEVEL(414),
	ERR_UNKNOWNCOMMAND(421),
	ERR_NOMOTD(422),
	ERR_NOADMININFO(423),
	ERR_FILEERROR(424),
	ERR_NONICKNAMEGIVEN(431),
	ERR_ERRONEUSNICKNAME(432),
	ERR_NICKNAMEINUSE(433),
	ERR_NICKCOLLISION(436),
	ERR_USERNOTINCHANNEL(441),
	ERR_NOTONCHANNEL(442),
	ERR_USERONCHANNEL(443),
	ERR_NOLOGIN(444),
	ERR_SUMMONDISABLED(445),
	ERR_USERSDISABLED(446),
	ERR_NOTREGISTERED(451),
	ERR_NEEDMOREPARAMS(461),
	ERR_ALREADYREGISTERED(462),
	ERR_NOPERMFORHOST(463),
	ERR_PASSWDMISMATCH(464),
	ERR_YOUREBANNEDCREEP(465),
	ERR_YOUWILLBEBANNED(466),
	ERR_KEYSET(467),
	ERR_CHANNELISFULL(471),
	ERR_UNKNOWNMODE(472),
	ERR_INVITEONLYCHAN(473),
	ERR_BANNEDFROMCHAN(474),
	ERR_BADCHANNELKEY(475),
	ERR_BADCHANMASK(476),
	ERR_NOPRIVILEGES(481),
	ERR_CHANOPRIVSNEEDED(482),
	ERR_CANTKILLSERVER(483),
	ERR_NOOPERHOST(491),
	ERR_NOSERVICEHOST(492),
	ERR_UMODEUNKNOWNFLAG(501),
	ERR_USERSDONTMATCH(502),
	;
	
	short m_code;
	boolean m_request = true;
	String m_name;
	
	private static Map<String, IRCCode> map;
	
	/**
	 * Constructor for requests.  Identifiers must be the same as the
	 * request name in the protocol.  If future development impedes
	 * request names to be named equal, a parameter with the command
	 * name will be added.
	 */
	IRCCode() {
		m_code = -1;
		m_request = true;
		m_name = name();
	}
	
	/**
	 * Constructor for replies.  We conserve names as per RFC-
	 * @param code
	 */
	IRCCode(int code) {
		m_code = (short) code;
		m_request = false;
		m_name = String.format("%03d", code);
	}
	
	/**
	 * Method to get the message code from the IRC message string that
	 * comes in the message.
	 * @param v the IRC string matching the MessageCode to look for.
	 * @return the MessageCode corresponding to the matched String.
	 */
	public static final IRCCode fromProto(String v) {
		if (map == null) { // this happens only once.
			map = new TreeMap<String, IRCCode>();
			for (IRCCode c: IRCCode.values())
				map.put(c.getName(), c);
		}
		return map.get(v);
	}
	
	/**
	 * Getter for the {@code request} {@code boolean} property.
	 * @return {@code true} if the MessageCode corresponds to a
	 * request code.
	 */
	public boolean isRequest() {
		return m_request;
	}
	
	/**
	 * Getter for the {@code code} attribute.
	 * @return the protocol {@code code} attribute in case
	 * of a response. it returns {@code -1} otherwise.
	 */
	public int getCode() {
		return m_code;
	}
	
	/**
	 * Getter for the {@code name} attribute.  Be careful that this
	 * is not the same as the {@link #name()} method.
	 * @return the {@code name} attribute.  This is the string value
	 * that gets into the message.  For requests it is equal to the
	 * String returned by the {@link #name()} method, but for responses
	 * it is equal to the {@code String} that represents the numeric three
	 * digit code of the response.
	 */
	public String getName() {
		return m_name;
	}
	
	/**
	 * Method to know if a response is an error message.
	 * @return {@code true} in case the {@link IRCCode} is a error
	 * response code.
	 */
	public boolean isError() {
		return isReply() && m_code >= 400;
	}
	
	/**
	 * Method to know if the message code is for a response message.
	 * @return {@code true} for replies.
	 */
	public boolean isReply() {
		return !m_request;
	}

} /* MessageCode */
