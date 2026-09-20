package kopo.poly2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OcrDTO {

    private String fileName;
    private String filePath;
    private String textFromImage;
}
