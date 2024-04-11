package com.mj.mysns.chat.entity;

import com.mj.mysns.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

@Entity
public class Chat extends BaseEntity {

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<ChatUser> users = new ArrayList<>();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Getter
    private ChatStatus status = ChatStatus.OPEN;

    public enum ChatStatus {
        OPEN, CLOSED
    }

    public void enterUser(ChatUser user) {
        user.enter(this);

        if (!this.users.contains(user)) {
            this.users.add(user);
        }
    }

    public void exitUser(ChatUser user) {
        user.exit(this);
        this.users.remove(user);
    }

    public void addMessage(ChatMessage message) {
        message.setChat(this);
        this.messages.add(message);
    }

    public List<ChatUser> getUsers() {
        return Collections.unmodifiableList(this.users);
    }

    public List<ChatMessage> getMessages() {
        return Collections.unmodifiableList(this.messages);
    }
}
