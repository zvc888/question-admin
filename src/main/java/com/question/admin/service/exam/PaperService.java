package com.question.admin.service.exam;

import com.baomidou.mybatisplus.extension.service.IService;
import com.question.admin.domain.entity.exam.Paper;
import com.question.admin.domain.vo.manager.exam.pape.ExamPaperEditRequestVM;

import javax.validation.Valid;

public interface PaperService extends IService<Paper> {
    ExamPaperEditRequestVM examPaperToVM(Long id);
    Paper savePaperFromVM(@Valid ExamPaperEditRequestVM model);
}
