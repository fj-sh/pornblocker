package com.pornblocker.pornblockerbackend.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "PORN_SITE")
public class PornSiteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "PORN_SITE_ID")
  private Long pornSiteId;

  @Column(name = "SITE_URL")
  private String siteUrl;

  public PornSiteEntity(Long pornSiteId, String siteUrl) {
    this.pornSiteId = pornSiteId;
    this.siteUrl = siteUrl;
  }

  public PornSiteEntity() {}

  public Long getPornSiteId() {
    return pornSiteId;
  }

  public void setPornSiteId(Long pornSiteId) {
    this.pornSiteId = pornSiteId;
  }

  public String getSiteUrl() {
    return siteUrl;
  }

  public void setSiteUrl(String siteUrl) {
    this.siteUrl = siteUrl;
  }

}
