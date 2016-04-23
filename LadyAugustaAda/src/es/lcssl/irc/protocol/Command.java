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
public enum Command {
	PASS(new Response[] {
			Response.ERR_NEEDMOREPARAMS, 
			Response.ERR_ALREADYREGISTERED,
	}),
	NICK(new Response[] {
			Response.ERR_NONICKNAMEGIVEN, 
			Response.ERR_ERRONEUSNICKNAME, 
			Response.ERR_NICKNAMEINUSE, 
			Response.ERR_NICKCOLLISION,
	}),
	USER(new Response[] {
			Response.ERR_NEEDMOREPARAMS,
			Response.ERR_ALREADYREGISTERED,
	}),
	SERVER(new Response[] {
			Response.ERR_ALREADYREGISTERED,
	}),
	OPER(new Response[] {
			Response.RPL_YOUREOPER,
			
			Response.ERR_NEEDMOREPARAMS,
			Response.ERR_NOOPERHOST,
			Response.ERR_PASSWDMISMATCH,
	}),
	QUIT(new Response[] {}),
	SQUIT(new Response[] {
			Response.ERR_NOPRIVILEGES,
			Response.ERR_NOSUCHSEVER,
	}),
	JOIN(new Response[] {
			Response.RPL_TOPIC,

			Response.ERR_NEEDMOREPARAMS,
			Response.ERR_INVITEONLYCHAN,
			Response.ERR_CHANNELISFULL,
			Response.ERR_NOSUCHCHANNEL,
			Response.ERR_BANNEDFROMCHAN,
			Response.ERR_BADCHANNELKEY,
			Response.ERR_BADCHANMASK,
			Response.ERR_TOOMANYCHANNELS,
	}),
	PART(new Response[] {
			Response.ERR_NEEDMOREPARAMS,
			Response.ERR_NOTONCHANNEL,
			Response.ERR_NOSUCHCHANNEL,
	}),
	MODE(new Response[] {
			Response.RPL_BANLIST,
			Response.RPL_CHANNELMODEIS,
			Response.RPL_ENDOFBANLIST,
			Response.RPL_UMODEIS,

			Response.ERR_NEEDMOREPARAMS,
			Response.ERR_CHANOPRIVSNEEDED,
			Response.ERR_NOTONCHANNEL,
			Response.ERR_UNKNOWNMODE,
			Response.ERR_USERSDONTMATCH,
			Response.ERR_UMODEUNKNOWNFLAG,
			Response.ERR_NOSUCHNICK,
			Response.ERR_KEYSET,
			Response.ERR_NOSUCHCHANNEL,
	}),
	TOPIC(new Response[] {
			Response.RPL_NOTOPIC,
			Response.RPL_TOPIC,

			Response.ERR_NEEDMOREPARAMS,
			Response.ERR_NOTONCHANNEL,
			Response.ERR_CHANOPRIVSNEEDED,
	}),
	NAMES(new Response[] {
			Response.RPL_NAMREPLY,
			Response.RPL_ENDOFNAMES,
	}),
	LIST(new Response[] {
			Response.ERR_NOSUCHSEVER,
			Response.RPL_LISTSTART,
			Response.RPL_LIST,
			Response.RPL_LISTEND,
	}),
	INVITE(new Response[] {
			Response.ERR_NEEDMOREPARAMS,
			Response.ERR_NOSUCHNICK,
			Response.ERR_NOTONCHANNEL,
			Response.ERR_USERONCHANNEL,
			Response.ERR_CHANOPRIVSNEEDED,
			Response.RPL_INVITING,
			Response.RPL_AWAY,
	}),
	KICK(new Response[] {
			Response.ERR_NEEDMOREPARAMS,
			Response.ERR_NOSUCHCHANNEL,
			Response.ERR_BADCHANMASK,
			Response.ERR_CHANOPRIVSNEEDED,
			Response.ERR_NOTONCHANNEL,
	}),
	VERSION(new Response[] {
			Response.ERR_NOSUCHSEVER,
			Response.RPL_VERSION,
	}),
	STATS(new Response[] {
			Response.ERR_NOSUCHSEVER,
			Response.RPL_STATSCLINE,
			Response.RPL_STATSNLINE,
			Response.RPL_STATSILINE,
			Response.RPL_STATSKLINE,
			Response.RPL_STATSQLINE,
			Response.RPL_STATSLLINE,
			Response.RPL_STATSLINKINFO,
			Response.RPL_STATSUPTIME,
			Response.RPL_STATSCOMMANDS,
			Response.RPL_STATSOLINE,
			Response.RPL_STATSHLINE,
			Response.RPL_ENDOFSTATS,
	}),
	LINKS(new Response[] {
			Response.ERR_NOSUCHSEVER,
			Response.RPL_LINKS,
			Response.RPL_ENDOFLINKS,
	}),
	TIME(new Response[] {
			Response.ERR_NOSUCHSEVER,
			Response.RPL_TIME,
	}),
	CONNECT(new Response[] {
			Response.ERR_NOSUCHSEVER,
			Response.ERR_NOPRIVILEGES,
			Response.ERR_NEEDMOREPARAMS,
	}),
	TRACE(new Response[] {
			Response.ERR_NOSUCHSEVER,
			Response.RPL_TRACELINK,
			Response.RPL_TRACECONNECTING,
			Response.RPL_TRACEHANDSHAKE,
			Response.RPL_TRACEUNKNOWN,
			Response.RPL_TRACEOPERATOR,
			Response.RPL_TRACEUSER,
			Response.RPL_TRACESERVER,
			Response.RPL_TRACENEWTYPE,
			Response.RPL_TRACECLASS,
	}),
	ADMIN(new Response[] {
			Response.ERR_NOSUCHSEVER,
			Response.RPL_ADMINME,
			Response.RPL_ADMINLOC1,
			Response.RPL_ADMINLOC2,
			Response.RPL_ADMINEMAIL,
	}),
	INFO(new Response[] {
			Response.ERR_NOSUCHSEVER,
			Response.RPL_INFO,
			Response.RPL_ENDOFINFO,
	}),
	PRIVMSG(new Response[] {
			Response.ERR_NORECIPIENT,
			Response.ERR_NOTEXTTOSEND,
			Response.ERR_CANNOTSENDTOCHAN,
			Response.ERR_NOTOPLEVEL,
			Response.ERR_WILDTOPLEVEL,
			Response.ERR_TOOMANYTARGETS,
			Response.ERR_NOSUCHNICK,
			Response.RPL_AWAY,
	}),
	NOTICE(new Response[] {
			Response.ERR_NORECIPIENT,
			Response.ERR_NOTEXTTOSEND,
			Response.ERR_CANNOTSENDTOCHAN,
			Response.ERR_NOTOPLEVEL,
			Response.ERR_WILDTOPLEVEL,
			Response.ERR_TOOMANYTARGETS,
			Response.ERR_NOSUCHNICK,
			Response.RPL_AWAY,
	}),
	WHO(new Response[] {
			Response.ERR_NOSUCHSEVER,
			Response.RPL_WHOREPLY,
			Response.RPL_ENDOFWHO,
	}),
	WHOIS(new Response[] {
			Response.ERR_NOSUCHSEVER,
			Response.ERR_NONICKNAMEGIVEN,
			Response.RPL_WHOISUSER,
			Response.RPL_WHOISCHANNELS,
			Response.RPL_WHOISSERVER,
			Response.RPL_AWAY,
			Response.RPL_WHOISOPERATOR,
			Response.RPL_WHOISIDLE,
			Response.ERR_NOSUCHNICK,
			Response.RPL_ENDOFWHOIS,
	}),
	WHOWAS(new Response[] {
			Response.ERR_NONICKNAMEGIVEN,
			Response.ERR_WASNOSUCHNICK,
			Response.RPL_WHOWASUSER,
			Response.RPL_WHOISSERVER,
			Response.RPL_ENDOFWHOWAS,
	}),
	KILL(new Response[] {
			Response.ERR_NOPRIVILEGES,
			Response.ERR_NEEDMOREPARAMS,
			Response.ERR_NOSUCHNICK,
			Response.ERR_CANTKILLSERVER,
	}),
	PING(new Response[] {
			Response.ERR_NOORIGIN,
			Response.ERR_NOSUCHSEVER,
	}),
	PONG(new Response[] {
			Response.ERR_NOORIGIN,
			Response.ERR_NOSUCHSEVER,
	}),
	ERROR(new Response[] {}),
	AWAY(new Response[] {
			Response.RPL_UNAWAY,
			Response.RPL_NOWAWAY,
	}),
	REHASH(new Response[] {
			Response.RPL_REHASHING,
			Response.ERR_NOPRIVILEGES,
	}),
	RESTART(new Response[] {
			Response.ERR_NOPRIVILEGES,
	}),
	SUMMON(new Response[] {
			Response.ERR_NORECIPIENT,
			Response.ERR_FILEERROR,
			Response.ERR_NOLOGIN,
			Response.ERR_NOSUCHSEVER,
			Response.RPL_SUMMONING,
	}),
	USERS(new Response[] {
			Response.ERR_NOSUCHSEVER,
			Response.ERR_FILEERROR,
			Response.RPL_USERSSTART,
			Response.RPL_USERS,
			Response.RPL_NOUSERS,
			Response.RPL_ENDOFUSERS,
			Response.ERR_USERSDISABLED,
	}),
	WALLOPS(new Response[] {
			Response.ERR_NEEDMOREPARAMS,
	}),
	USERHOST(new Response[] {
			Response.RPL_USERHOST,
			Response.ERR_NEEDMOREPARAMS,
	}),
	ISON(new Response[] {
			Response.RPL_ISON,
			Response.ERR_NEEDMOREPARAMS,
	}),
	;
	Response[] m_resp;
	Command(Response[] resp) {
		m_resp = resp;
	}
	Response[] getResponses() {
		return m_resp;
	}
}
