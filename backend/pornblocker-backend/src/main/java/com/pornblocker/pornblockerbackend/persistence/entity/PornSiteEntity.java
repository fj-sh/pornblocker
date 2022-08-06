package com.pornblocker.pornblockerbackend.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PORN_SITE")
public class PornSiteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "PORN_SITE_ID")
  private Integer pornSiteId;

  @Column(name = "SITE_URL")
  private String siteUrl;

  public Integer getPornSiteId() {
    return pornSiteId;
  }

  public void setPornSiteId(Integer pornSiteId) {
    this.pornSiteId = pornSiteId;
  }

  public String getSiteUrl() {
    return siteUrl;
  }

  public void setSiteUrl(String siteUrl) {
    this.siteUrl = siteUrl;
  }

}
