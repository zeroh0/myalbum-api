package com.myalbum.common.storage.repository;

import com.myalbum.common.storage.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepository extends JpaRepository<UploadFile, Long> {
}
