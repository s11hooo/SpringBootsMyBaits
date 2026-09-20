package kopo.poly2.service;

import kopo.poly2.dto.OcrDTO;

public interface iOcrService {

    OcrDTO getReadforImageText(OcrDTO pDTO) throws Exception;
}
