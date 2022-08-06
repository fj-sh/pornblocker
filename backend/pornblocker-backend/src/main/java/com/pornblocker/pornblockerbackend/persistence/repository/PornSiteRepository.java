package com.pornblocker.pornblockerbackend.persistence.repository;

import com.pornblocker.pornblockerbackend.persistence.entity.PornSiteEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PornSiteRepository extends JpaRepository<PornSiteEntity, Integer> { }
