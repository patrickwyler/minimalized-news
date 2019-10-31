package ch.wyler.minimalizednews.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleFullDto {

	Long id;
	String title;
	String rawText;
	String text;
	Date creationDate;
	Date lastModifiedDate;
	String sourceName;
	String sourceUrl;
	boolean approved;
	Set<TagDto> tags = new HashSet<>();
}
