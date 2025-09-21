package org.github.adisputraa.qshort.data;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class UrlMapping extends PanacheEntity {

    @Column(unique = true, length = 8)
    public String shortCode;

    @Column(name = "original_url", nullable = false, length = 2048)
    public String originalUrl;

    /**
     * Metode pencarian khusus sebagai bagian dari pola Active Record.
     * Jauh lebih sederhana daripada membuat kelas Repository terpisah.
     */

    public static UrlMapping findByShortCode(String shortCode) {
        return find("shortCode", shortCode).firstResult();
    }
}
