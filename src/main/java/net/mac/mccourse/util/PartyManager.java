package net.mac.mccourse.util;

import net.mac.mccourse.Party;

import java.util.*;

public class PartyManager {
    private static final Map<String, Party> parties = new HashMap<>();
    private static final Map<String, String> pendingInvites = new HashMap<>();

    public static void invitePlayer(String inviter, String invitee) {
        pendingInvites.put(invitee, inviter);
    }

    public static void acceptInvite(String invite) {
        String inviter = pendingInvites.remove(invite);
        if (inviter != null) {
            Party party = parties.computeIfAbsent(inviter, k -> new Party());
            party.addMember(invite);
            checkAndRemoveSingleMemberParties();
        }
    }

    public static void requestJoin(String requester, String target) {
    }

    public static Collection<Party> getAllParties() {
        return parties.values();
    }

    private static void checkAndRemoveSingleMemberParties() {
        parties.entrySet().removeIf(entry -> entry.getValue().getMembers().size() == 1);
    }
}
