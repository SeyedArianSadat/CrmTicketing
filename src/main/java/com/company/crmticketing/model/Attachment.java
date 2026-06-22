package com.company.crmticketing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
@Entity(name = "attachmentEntity")
@Table(name = "attachments")


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Attachment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentId;

    @Column(name = "file_name",length = 500)
    private String fileName;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ticket_id",nullable = false)
    private Ticket ticket;
}
