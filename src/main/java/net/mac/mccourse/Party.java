package net.mac.mccourse;

import java.util.HashSet;
import java.util.Set;

public class Party {
    private final Set<String> members;

    public Party() {
        this.members = new HashSet<>();
    }

    public void addMember(String member) {
        members.add(member);
    }

    public Set<String> getMembers() {
        return members;
    }
}