package io.arusland.k8.dto;

import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceOwner;
import io.arusland.k8.source.SourceType;

/**
 * Created by ruslan on 21.10.2015.
 */
public class SearchSourceDto {
    private Long id;
    private String type;
    private String path;
    private String owner;
    private String lastActiveCatalog;

    public SearchSourceDto() {
    }

    public SearchSourceDto(String type, String path, String owner, String lastActiveCatalog, Long id) {
        this.id = id;
        this.type = type;
        this.path = path;
        this.owner = owner;
        this.lastActiveCatalog = lastActiveCatalog;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLastActiveCatalog() {
        return lastActiveCatalog;
    }

    public void setLastActiveCatalog(String lastActiveCatalog) {
        this.lastActiveCatalog = lastActiveCatalog;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SearchSource toEntity() {
        return new SearchSource(SourceType.valueOf(this.getType()),
                this.getPath(),
                this.getLastActiveCatalog(),
                new SourceOwner(this.getOwner()), this.getId());
    }

    public static SearchSourceDto fromEntity(SearchSource source) {
        return new SearchSourceDto(source.getType().toString(), source.getPath(),
                source.getOwner().getName(), source.getLastActiveCatalog(), source.getId());
    }
}
