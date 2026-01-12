package com.example.demo.entity.cakeTableDto.banners;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link com.example.demo.entity.cakeTable.Banner}
 */
public class BannerDto implements Serializable {
    private final Integer id;
    private final String title;
    private final String imageUrl;
    private final String linkUrl;
    private final String linkType;
    private final Integer linkId;
    private final Integer sortOrder;
    private final String status;
    private final Instant startTime;
    private final Instant endTime;
    private final Instant createdAt;

    public BannerDto(Integer id, String title, String imageUrl, String linkUrl, 
                    String linkType, Integer linkId, Integer sortOrder, 
                    String status, Instant startTime, Instant endTime, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.linkType = linkType;
        this.linkId = linkId;
        this.sortOrder = sortOrder;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public String getLinkType() {
        return linkType;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public String getStatus() {
        return status;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BannerDto entity = (BannerDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.title, entity.title) &&
                Objects.equals(this.imageUrl, entity.imageUrl) &&
                Objects.equals(this.linkUrl, entity.linkUrl) &&
                Objects.equals(this.linkType, entity.linkType) &&
                Objects.equals(this.linkId, entity.linkId) &&
                Objects.equals(this.sortOrder, entity.sortOrder) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.startTime, entity.startTime) &&
                Objects.equals(this.endTime, entity.endTime) &&
                Objects.equals(this.createdAt, entity.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, imageUrl, linkUrl, linkType, linkId, sortOrder, status, startTime, endTime, createdAt);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "title = " + title + ", " +
                "imageUrl = " + imageUrl + ", " +
                "linkUrl = " + linkUrl + ", " +
                "linkType = " + linkType + ", " +
                "linkId = " + linkId + ", " +
                "sortOrder = " + sortOrder + ", " +
                "status = " + status + ", " +
                "startTime = " + startTime + ", " +
                "endTime = " + endTime + ", " +
                "createdAt = " + createdAt + ")";
    }
}