package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "notifies")
@Getter
@Setter
public class Notify extends Base {
    @Size(max = 250)
    @Column(name = "notify_content", length = 250)
    private String notifyContent;

    @Size(max = 250)
    @Column(name = "ref_address", length = 250)
    private String refAddress;

    @ColumnDefault("false")
    @Column(name = "is_read")
    private Boolean isRead;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
