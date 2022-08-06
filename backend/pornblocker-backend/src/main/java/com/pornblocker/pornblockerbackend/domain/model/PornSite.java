package com.pornblocker.pornblockerbackend.domain.model;

import com.pornblocker.pornblockerbackend.persistence.entity.PornSiteEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PornSite {
  private Integer pornSiteId;
  private String siteUrl;

  public PornSite(PornSiteEntity entity) {
    this(entity.getPornSiteId(), entity.getSiteUrl());
  }
}
