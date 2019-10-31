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
public class ArticleDto {

	String title;
	String text;
	Date creationDate;
	String sourceName;
	String sourceUrl;
	Set<TagDto> tags = new HashSet<>();
}
