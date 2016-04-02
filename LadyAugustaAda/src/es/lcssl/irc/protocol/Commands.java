/**
 * 
 */
package es.lcssl.irc.protocol;

/**
 * @author luis
 *
 */
public enum Commands {
	PASS(new Responses[] {
			Responses.ERR_NEEDMOREPARAMS, 
			Responses.ERR_ALREADYREGISTERED,
	}),
	NICK(new Responses[] {
			Responses.ERR_NONICKNAMEGIVEN, 
			Responses.ERR_ERRONEUSNICKNAME, 
			Responses.ERR_NICKNAMEINUSE, 
			Responses.ERR_NICKCOLLISION,
	}),
	USER(new Responses[] {
			Responses.ERR_NEEDMOREPARAMS,
			Responses.ERR_ALREADYREGISTERED,
	}),
	SERVER(new Responses[] {
			Responses.ERR_ALREADYREGISTERED,
	}),
	OPER(new Responses[] {
			Responses.RPL_YOUREOPER,
			
			Responses.ERR_NEEDMOREPARAMS,
			Responses.ERR_NOOPERHOST,
			Responses.ERR_PASSWDMISMATCH,
	}),
	QUIT(new Responses[] {}),
	SQUIT(new Responses[] {
			Responses.ERR_NOPRIVILEGES,
			Responses.ERR_NOSUCHSEVER,
	}),
	JOIN(new Responses[] {
			Responses.RPL_TOPIC,

			Responses.ERR_NEEDMOREPARAMS,
			Responses.ERR_INVITEONLYCHAN,
			Responses.ERR_CHANNELISFULL,
			Responses.ERR_NOSUCHCHANNEL,
			Responses.ERR_BANNEDFROMCHAN,
			Responses.ERR_BADCHANNELKEY,
			Responses.ERR_BADCHANMASK,
			Responses.ERR_TOOMANYCHANNELS,
	}),
	PART(new Responses[] {
			Responses.ERR_NEEDMOREPARAMS,
			Responses.ERR_NOTONCHANNEL,
			Responses.ERR_NOSUCHCHANNEL,
	}),
	MODE(new Responses[] {
			Responses.RPL_BANLIST,
			Responses.RPL_CHANNELMODEIS,
			Responses.RPL_ENDOFBANLIST,
			Responses.RPL_UMODEIS,

			Responses.ERR_NEEDMOREPARAMS,
			Responses.ERR_CHANOPRIVSNEEDED,
			Responses.ERR_NOTONCHANNEL,
			Responses.ERR_UNKNOWNMODE,
			Responses.ERR_USERSDONTMATCH,
			Responses.ERR_UMODEUNKNOWNFLAG,
			Responses.ERR_NOSUCHNICK,
			Responses.ERR_KEYSET,
			Responses.ERR_NOSUCHCHANNEL,
	}),
	TOPIC(new Responses[] {
			Responses.RPL_NOTOPIC,
			Responses.RPL_TOPIC,

			Responses.ERR_NEEDMOREPARAMS,
			Responses.ERR_NOTONCHANNEL,
			Responses.ERR_CHANOPRIVSNEEDED,
	}),
	NAMES(new Responses[] {
			Responses.RPL_NAMREPLY,
			Responses.RPL_ENDOFNAMES,
	}),
	LIST(new Responses[] {
			Responses.ERR_NOSUCHSEVER,
			Responses.RPL_LISTSTART,
			Responses.RPL_LIST,
			Responses.RPL_LISTEND,
	}),
	INVITE(new Responses[] {
			Responses.ERR_NEEDMOREPARAMS,
			Responses.ERR_NOSUCHNICK,
			Responses.ERR_NOTONCHANNEL,
			Responses.ERR_USERONCHANNEL,
			Responses.ERR_CHANOPRIVSNEEDED,
			Responses.RPL_INVITING,
			Responses.RPL_AWAY,
	}),
	KICK(new Responses[] {
			Responses.ERR_NEEDMOREPARAMS,
			Responses.ERR_NOSUCHCHANNEL,
			Responses.ERR_BADCHANMASK,
			Responses.ERR_CHANOPRIVSNEEDED,
			Responses.ERR_NOTONCHANNEL,
	}),
	VERSION(new Responses[] {
			Responses.ERR_NOSUCHSEVER,
			Responses.RPL_VERSION,
	}),
	STATS(new Responses[] {
			Responses.ERR_NOSUCHSEVER,
			Responses.RPL_STATSCLINE,
			Responses.RPL_STATSNLINE,
			Responses.RPL_STATSILINE,
			Responses.RPL_STATSKLINE,
			Responses.RPL_STATSQLINE,
			Responses.RPL_STATSLLINE,
			Responses.RPL_STATSLINKINFO,
			Responses.RPL_STATSUPTIME,
			Responses.RPL_STATSCOMMANDS,
			Responses.RPL_STATSOLINE,
			Responses.RPL_STATSHLINE,
			Responses.RPL_ENDOFSTATS,
	}),
	LINKS(new Responses[] {
			Responses.ERR_NOSUCHSEVER,
			Responses.RPL_LINKS,
			Responses.RPL_ENDOFLINKS,
	}),
	TIME(new Responses[] {
			Responses.ERR_NOSUCHSEVER,
			Responses.RPL_TIME,
	}),
	CONNECT(new Responses[] {
			Responses.ERR_NOSUCHSEVER,
			Responses.ERR_NOPRIVILEGES,
			Responses.ERR_NEEDMOREPARAMS,
	}),
	TRACE(new Responses[] {
			Responses.ERR_NOSUCHSEVER,
			Responses.RPL_TRACELINK,
			Responses.RPL_TRACECONNECTING,
			Responses.RPL_TRACEHANDSHAKE,
			Responses.RPL_TRACEUNKNOWN,
			Responses.RPL_TRACEOPERATOR,
			Responses.RPL_TRACEUSER,
			Responses.RPL_TRACESERVER,
			Responses.RPL_TRACENEWTYPE,
			Responses.RPL_TRACECLASS,
	}),
	ADMIN(new Responses[] {
			Responses.ERR_NOSUCHSEVER,
			Responses.RPL_ADMINME,
			Responses.RPL_ADMINLOC1,
			Responses.RPL_ADMINLOC2,
			Responses.RPL_ADMINEMAIL,
	}),
	INFO(new Responses[] {
			Responses.ERR_NOSUCHSEVER,
			Responses.RPL_INFO,
			Responses.RPL_ENDOFINFO,
	}),
	PRIVMSG(new Responses[] {
			Responses.ERR_NORECIPIENT,
			Responses.ERR_NOTEXTTOSEND,
			Responses.ERR_CANNOTSENDTOCHAN,
			Responses.ERR_NOTOPLEVEL,
			Responses.ERR_WILDTOPLEVEL,
			Responses.ERR_TOOMANYTARGETS,
			Responses.ERR_NOSUCHNICK,
			Responses.RPL_AWAY,
	}),
	NOTICE(new Responses[] {
			Responses.ERR_NORECIPIENT,
			Responses.ERR_NOTEXTTOSEND,
			Responses.ERR_CANNOTSENDTOCHAN,
			Responses.ERR_NOTOPLEVEL,
			Responses.ERR_WILDTOPLEVEL,
			Responses.ERR_TOOMANYTARGETS,
			Responses.ERR_NOSUCHNICK,
			Responses.RPL_AWAY,
	}),
	WHO(new Responses[] {
			Responses.ERR_NOSUCHSEVER,
			Responses.RPL_WHOREPLY,
			Responses.RPL_ENDOFWHO,
	}),
	WHOIS(new Responses[] {
			Responses.ERR_NOSUCHSEVER,
			Responses.ERR_NONICKNAMEGIVEN,
			Responses.RPL_WHOISUSER,
			Responses.RPL_WHOISCHANNELS,
			Responses.RPL_WHOISSERVER,
			Responses.RPL_AWAY,
			Responses.RPL_WHOISOPERATOR,
			Responses.RPL_WHOISIDLE,
			Responses.ERR_NOSUCHNICK,
			Responses.RPL_ENDOFWHOIS,
	}),
	WHOWAS(new Responses[] {
			Responses.ERR_NONICKNAMEGIVEN,
			Responses.ERR_WASNOSUCHNICK,
			Responses.RPL_WHOWASUSER,
			Responses.RPL_WHOISSERVER,
			Responses.RPL_ENDOFWHOWAS,
	}),
	KILL(new Responses[] {
			Responses.ERR_NOPRIVILEGES,
			Responses.ERR_NEEDMOREPARAMS,
			Responses.ERR_NOSUCHNICK,
			Responses.ERR_CANTKILLSERVER,
	}),
	PING(new Responses[] {
			Responses.ERR_NOORIGIN,
			Responses.ERR_NOSUCHSEVER,
	}),
	PONG(new Responses[] {
			Responses.ERR_NOORIGIN,
			Responses.ERR_NOSUCHSEVER,
	}),
	ERROR(new Responses[] {}),
	AWAY(new Responses[] {
			Responses.RPL_UNAWAY,
			Responses.RPL_NOWAWAY,
	}),
	REHASH(new Responses[] {
			Responses.RPL_REHASHING,
			Responses.ERR_NOPRIVILEGES,
	}),
	RESTART(new Responses[] {
			Responses.ERR_NOPRIVILEGES,
	}),
	SUMMON(new Responses[] {
			Responses.ERR_NORECIPIENT,
			Responses.ERR_FILEERROR,
			Responses.ERR_NOLOGIN,
			Responses.ERR_NOSUCHSEVER,
			Responses.RPL_SUMMONING,
	}),
	USERS(new Responses[] {
			Responses.ERR_NOSUCHSEVER,
			Responses.ERR_FILEERROR,
			Responses.RPL_USERSSTART,
			Responses.RPL_USERS,
			Responses.RPL_NOUSERS,
			Responses.RPL_ENDOFUSERS,
			Responses.ERR_USERSDISABLED,
	}),
	WALLOPS(new Responses[] {
			Responses.ERR_NEEDMOREPARAMS,
	}),
	USERHOST(new Responses[] {
			Responses.RPL_USERHOST,
			Responses.ERR_NEEDMOREPARAMS,
	}),
	ISON(new Responses[] {
			Responses.RPL_ISON,
			Responses.ERR_NEEDMOREPARAMS,
	}),
	;
	Responses[] m_resp;
	Commands(Responses[] resp) {
		m_resp = resp;
	}
	Responses[] getResponses() {
		return m_resp;
	}
}
