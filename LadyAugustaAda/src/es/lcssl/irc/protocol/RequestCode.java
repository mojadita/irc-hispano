/*
 * Id: $Id$
 * Author: Luis Colorado <lc@luiscoloradosistemas.com>
 * Date: 22 de abr. de 2016, 20:43:13
 * Project: LadyAugustaAda
 * Package: es.lcssl.irc.protocol
 * Copyright: (C) 2016 LUIS COLORADO.  All rights reserved.
 */
package es.lcssl.irc.protocol;

/**
 * Command available to clients in IRC Protocol. See
 * <a href="https://tools.ietf.org/html/rfc2812">RFC-2812</a>
 * for the specification of IRC protocol.
 * 
 * @author Luis Colorado {@code <luiscoloradourcola@gmail.com>}
 *
 */
public enum RequestCode {
	PASS(new ResponseCode[] {
			ResponseCode.ERR_NEEDMOREPARAMS, 
			ResponseCode.ERR_ALREADYREGISTERED,
	}),
	NICK(new ResponseCode[] {
			ResponseCode.ERR_NONICKNAMEGIVEN, 
			ResponseCode.ERR_ERRONEUSNICKNAME, 
			ResponseCode.ERR_NICKNAMEINUSE, 
			ResponseCode.ERR_NICKCOLLISION,
	}),
	USER(new ResponseCode[] {
			ResponseCode.ERR_NEEDMOREPARAMS,
			ResponseCode.ERR_ALREADYREGISTERED,
	}),
	SERVER(new ResponseCode[] {
			ResponseCode.ERR_ALREADYREGISTERED,
	}),
	OPER(new ResponseCode[] {
			ResponseCode.RPL_YOUREOPER,
			
			ResponseCode.ERR_NEEDMOREPARAMS,
			ResponseCode.ERR_NOOPERHOST,
			ResponseCode.ERR_PASSWDMISMATCH,
	}),
	QUIT(new ResponseCode[] {}),
	SQUIT(new ResponseCode[] {
			ResponseCode.ERR_NOPRIVILEGES,
			ResponseCode.ERR_NOSUCHSEVER,
	}),
	JOIN(new ResponseCode[] {
			ResponseCode.RPL_TOPIC,

			ResponseCode.ERR_NEEDMOREPARAMS,
			ResponseCode.ERR_INVITEONLYCHAN,
			ResponseCode.ERR_CHANNELISFULL,
			ResponseCode.ERR_NOSUCHCHANNEL,
			ResponseCode.ERR_BANNEDFROMCHAN,
			ResponseCode.ERR_BADCHANNELKEY,
			ResponseCode.ERR_BADCHANMASK,
			ResponseCode.ERR_TOOMANYCHANNELS,
	}),
	PART(new ResponseCode[] {
			ResponseCode.ERR_NEEDMOREPARAMS,
			ResponseCode.ERR_NOTONCHANNEL,
			ResponseCode.ERR_NOSUCHCHANNEL,
	}),
	MODE(new ResponseCode[] {
			ResponseCode.RPL_BANLIST,
			ResponseCode.RPL_CHANNELMODEIS,
			ResponseCode.RPL_ENDOFBANLIST,
			ResponseCode.RPL_UMODEIS,

			ResponseCode.ERR_NEEDMOREPARAMS,
			ResponseCode.ERR_CHANOPRIVSNEEDED,
			ResponseCode.ERR_NOTONCHANNEL,
			ResponseCode.ERR_UNKNOWNMODE,
			ResponseCode.ERR_USERSDONTMATCH,
			ResponseCode.ERR_UMODEUNKNOWNFLAG,
			ResponseCode.ERR_NOSUCHNICK,
			ResponseCode.ERR_KEYSET,
			ResponseCode.ERR_NOSUCHCHANNEL,
	}),
	TOPIC(new ResponseCode[] {
			ResponseCode.RPL_NOTOPIC,
			ResponseCode.RPL_TOPIC,

			ResponseCode.ERR_NEEDMOREPARAMS,
			ResponseCode.ERR_NOTONCHANNEL,
			ResponseCode.ERR_CHANOPRIVSNEEDED,
	}),
	NAMES(new ResponseCode[] {
			ResponseCode.RPL_NAMREPLY,
			ResponseCode.RPL_ENDOFNAMES,
	}),
	LIST(new ResponseCode[] {
			ResponseCode.ERR_NOSUCHSEVER,
			ResponseCode.RPL_LISTSTART,
			ResponseCode.RPL_LIST,
			ResponseCode.RPL_LISTEND,
	}),
	INVITE(new ResponseCode[] {
			ResponseCode.ERR_NEEDMOREPARAMS,
			ResponseCode.ERR_NOSUCHNICK,
			ResponseCode.ERR_NOTONCHANNEL,
			ResponseCode.ERR_USERONCHANNEL,
			ResponseCode.ERR_CHANOPRIVSNEEDED,
			ResponseCode.RPL_INVITING,
			ResponseCode.RPL_AWAY,
	}),
	KICK(new ResponseCode[] {
			ResponseCode.ERR_NEEDMOREPARAMS,
			ResponseCode.ERR_NOSUCHCHANNEL,
			ResponseCode.ERR_BADCHANMASK,
			ResponseCode.ERR_CHANOPRIVSNEEDED,
			ResponseCode.ERR_NOTONCHANNEL,
	}),
	VERSION(new ResponseCode[] {
			ResponseCode.ERR_NOSUCHSEVER,
			ResponseCode.RPL_VERSION,
	}),
	STATS(new ResponseCode[] {
			ResponseCode.ERR_NOSUCHSEVER,
			ResponseCode.RPL_STATSCLINE,
			ResponseCode.RPL_STATSNLINE,
			ResponseCode.RPL_STATSILINE,
			ResponseCode.RPL_STATSKLINE,
			ResponseCode.RPL_STATSQLINE,
			ResponseCode.RPL_STATSLLINE,
			ResponseCode.RPL_STATSLINKINFO,
			ResponseCode.RPL_STATSUPTIME,
			ResponseCode.RPL_STATSCOMMANDS,
			ResponseCode.RPL_STATSOLINE,
			ResponseCode.RPL_STATSHLINE,
			ResponseCode.RPL_ENDOFSTATS,
	}),
	LINKS(new ResponseCode[] {
			ResponseCode.ERR_NOSUCHSEVER,
			ResponseCode.RPL_LINKS,
			ResponseCode.RPL_ENDOFLINKS,
	}),
	TIME(new ResponseCode[] {
			ResponseCode.ERR_NOSUCHSEVER,
			ResponseCode.RPL_TIME,
	}),
	CONNECT(new ResponseCode[] {
			ResponseCode.ERR_NOSUCHSEVER,
			ResponseCode.ERR_NOPRIVILEGES,
			ResponseCode.ERR_NEEDMOREPARAMS,
	}),
	TRACE(new ResponseCode[] {
			ResponseCode.ERR_NOSUCHSEVER,
			ResponseCode.RPL_TRACELINK,
			ResponseCode.RPL_TRACECONNECTING,
			ResponseCode.RPL_TRACEHANDSHAKE,
			ResponseCode.RPL_TRACEUNKNOWN,
			ResponseCode.RPL_TRACEOPERATOR,
			ResponseCode.RPL_TRACEUSER,
			ResponseCode.RPL_TRACESERVER,
			ResponseCode.RPL_TRACENEWTYPE,
			ResponseCode.RPL_TRACECLASS,
	}),
	ADMIN(new ResponseCode[] {
			ResponseCode.ERR_NOSUCHSEVER,
			ResponseCode.RPL_ADMINME,
			ResponseCode.RPL_ADMINLOC1,
			ResponseCode.RPL_ADMINLOC2,
			ResponseCode.RPL_ADMINEMAIL,
	}),
	INFO(new ResponseCode[] {
			ResponseCode.ERR_NOSUCHSEVER,
			ResponseCode.RPL_INFO,
			ResponseCode.RPL_ENDOFINFO,
	}),
	PRIVMSG(new ResponseCode[] {
			ResponseCode.ERR_NORECIPIENT,
			ResponseCode.ERR_NOTEXTTOSEND,
			ResponseCode.ERR_CANNOTSENDTOCHAN,
			ResponseCode.ERR_NOTOPLEVEL,
			ResponseCode.ERR_WILDTOPLEVEL,
			ResponseCode.ERR_TOOMANYTARGETS,
			ResponseCode.ERR_NOSUCHNICK,
			ResponseCode.RPL_AWAY,
	}),
	NOTICE(new ResponseCode[] {
			ResponseCode.ERR_NORECIPIENT,
			ResponseCode.ERR_NOTEXTTOSEND,
			ResponseCode.ERR_CANNOTSENDTOCHAN,
			ResponseCode.ERR_NOTOPLEVEL,
			ResponseCode.ERR_WILDTOPLEVEL,
			ResponseCode.ERR_TOOMANYTARGETS,
			ResponseCode.ERR_NOSUCHNICK,
			ResponseCode.RPL_AWAY,
	}),
	WHO(new ResponseCode[] {
			ResponseCode.ERR_NOSUCHSEVER,
			ResponseCode.RPL_WHOREPLY,
			ResponseCode.RPL_ENDOFWHO,
	}),
	WHOIS(new ResponseCode[] {
			ResponseCode.ERR_NOSUCHSEVER,
			ResponseCode.ERR_NONICKNAMEGIVEN,
			ResponseCode.RPL_WHOISUSER,
			ResponseCode.RPL_WHOISCHANNELS,
			ResponseCode.RPL_WHOISSERVER,
			ResponseCode.RPL_AWAY,
			ResponseCode.RPL_WHOISOPERATOR,
			ResponseCode.RPL_WHOISIDLE,
			ResponseCode.ERR_NOSUCHNICK,
			ResponseCode.RPL_ENDOFWHOIS,
	}),
	WHOWAS(new ResponseCode[] {
			ResponseCode.ERR_NONICKNAMEGIVEN,
			ResponseCode.ERR_WASNOSUCHNICK,
			ResponseCode.RPL_WHOWASUSER,
			ResponseCode.RPL_WHOISSERVER,
			ResponseCode.RPL_ENDOFWHOWAS,
	}),
	KILL(new ResponseCode[] {
			ResponseCode.ERR_NOPRIVILEGES,
			ResponseCode.ERR_NEEDMOREPARAMS,
			ResponseCode.ERR_NOSUCHNICK,
			ResponseCode.ERR_CANTKILLSERVER,
	}),
	PING(new ResponseCode[] {
			ResponseCode.ERR_NOORIGIN,
			ResponseCode.ERR_NOSUCHSEVER,
	}),
	PONG(new ResponseCode[] {
			ResponseCode.ERR_NOORIGIN,
			ResponseCode.ERR_NOSUCHSEVER,
	}),
	ERROR(new ResponseCode[] {}),
	AWAY(new ResponseCode[] {
			ResponseCode.RPL_UNAWAY,
			ResponseCode.RPL_NOWAWAY,
	}),
	REHASH(new ResponseCode[] {
			ResponseCode.RPL_REHASHING,
			ResponseCode.ERR_NOPRIVILEGES,
	}),
	RESTART(new ResponseCode[] {
			ResponseCode.ERR_NOPRIVILEGES,
	}),
	SUMMON(new ResponseCode[] {
			ResponseCode.ERR_NORECIPIENT,
			ResponseCode.ERR_FILEERROR,
			ResponseCode.ERR_NOLOGIN,
			ResponseCode.ERR_NOSUCHSEVER,
			ResponseCode.RPL_SUMMONING,
	}),
	USERS(new ResponseCode[] {
			ResponseCode.ERR_NOSUCHSEVER,
			ResponseCode.ERR_FILEERROR,
			ResponseCode.RPL_USERSSTART,
			ResponseCode.RPL_USERS,
			ResponseCode.RPL_NOUSERS,
			ResponseCode.RPL_ENDOFUSERS,
			ResponseCode.ERR_USERSDISABLED,
	}),
	WALLOPS(new ResponseCode[] {
			ResponseCode.ERR_NEEDMOREPARAMS,
	}),
	USERHOST(new ResponseCode[] {
			ResponseCode.RPL_USERHOST,
			ResponseCode.ERR_NEEDMOREPARAMS,
	}),
	ISON(new ResponseCode[] {
			ResponseCode.RPL_ISON,
			ResponseCode.ERR_NEEDMOREPARAMS,
	}),
	;
	ResponseCode[] m_resp;
	RequestCode(ResponseCode[] resp) {
		m_resp = resp;
	}
	ResponseCode[] getResponses() {
		return m_resp;
	}
}
