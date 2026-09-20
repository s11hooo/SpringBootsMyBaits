package kopo.poly2.controller;

import kopo.poly2.dto.OcrDTO;
import kopo.poly2.service.iOcrService;
import kopo.poly2.utill.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@RequestMapping(value = "/ocr")
@RequiredArgsConstructor
@Controller
public class OcrController {

    private final iOcrService ocrService;

    @Value("${ocr.upload.path}")
    private String uploadPath;

    @GetMapping(value = "uploadImage")
    public String uploadImage() throws Exception {
        log.info("{}.uploadImage Start!", this.getClass().getName());
        log.info("{}.uploadImage End!", this.getClass().getName());

        return "ocr/uploadImage";
    }

    @PostMapping(value = "readImageText")
    public String readImageText(@RequestParam("imageFile") MultipartFile imageFile, ModelMap model) throws Exception {
        log.info("{}.readImageText Start!", this.getClass().getName());

        OcrDTO rDTO;

        if (imageFile == null || imageFile.isEmpty()) {
            rDTO = new OcrDTO();
            rDTO.setTextFromImage("No image file was uploaded.");
            model.addAttribute("rDTO", rDTO);
            return "ocr/readImageText";
        }

        String saveFilePath = FileUtil.saveFile(imageFile, uploadPath);

        OcrDTO pDTO = new OcrDTO();
        pDTO.setFileName(imageFile.getOriginalFilename());
        pDTO.setFilePath(saveFilePath);

        rDTO = Optional.ofNullable(ocrService.getReadforImageText(pDTO)).orElseGet(OcrDTO::new);
        rDTO.setFileName(imageFile.getOriginalFilename());
        rDTO.setFilePath(saveFilePath);

        model.addAttribute("rDTO", rDTO);

        log.info("{}.readImageText End!", this.getClass().getName());

        return "ocr/readImageText";
    }
}
