package com.mj.mysns.chat.entity;

import com.mj.mysns.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter

@Entity
@Table(indexes = @Index(columnList = "username, chat_id"))
public class ChatUser extends BaseEntity {

    @ManyToOne
    private Chat chat;

    private String username;

    public ChatUser(String username) {
        this.username = username;
    }

    protected void enter(Chat chat) {
        this.chat = chat;
    }

    protected void exit(Chat chat) {
        this.chat = null;
    }

}
