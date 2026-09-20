package kopo.poly2.service.impl;

import kopo.poly2.dto.OcrDTO;
import kopo.poly2.service.iOcrService;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
public class OcrService implements iOcrService {

    @Value("${ocr.model.path}")
    private String modelPath;

    @Override
    public OcrDTO getReadforImageText(OcrDTO pDTO) throws Exception {
        log.info("{}.getReadforImageText Start!", this.getClass().getName());

        String filePath = pDTO.getFilePath();
        log.info("filePath: {}", filePath);
        log.info("modelPath: {}", modelPath);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(modelPath);
        tesseract.setLanguage("kor+eng");

        String textFromImage = tesseract.doOCR(new File(filePath));
        pDTO.setTextFromImage(textFromImage);

        log.info("{}.getReadforImageText End!", this.getClass().getName());

        return pDTO;
    }
}
