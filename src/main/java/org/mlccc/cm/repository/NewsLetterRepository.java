package org.mlccc.cm.repository;

import org.mlccc.cm.domain.NewsLetter;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the NewsLetter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NewsLetterRepository extends JpaRepository<NewsLetter,Long> {
    @Query("select nl from NewsLetter nl order by nl.uploadDate desc")
    List<NewsLetter> findAllSortByUploadDateDesc();
}
