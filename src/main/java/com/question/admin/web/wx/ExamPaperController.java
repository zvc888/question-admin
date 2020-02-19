package com.question.admin.web.wx;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.question.admin.domain.entity.exam.Paper;
import com.question.admin.domain.entity.exam.Subject;
import com.question.admin.domain.vo.Result;
import com.question.admin.domain.vo.manager.exam.pape.ExamPaperEditRequestVM;
import com.question.admin.service.exam.PaperService;
import com.question.admin.service.exam.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;


@RestController
@RequestMapping(value = "/api/wx/student/")
@AllArgsConstructor
@ResponseBody
public class ExamPaperController extends BaseWXApiController {

    private final SubjectService subjectService;
    private final PaperService paperService;

    @RequestMapping(value = "/subject", method = RequestMethod.POST)
    public Result querySubjectList(@Valid @NotBlank Long categoryId){
        QueryWrapper<Subject> categoryBean =  new QueryWrapper();
        categoryBean.eq("status", 1);
        if(null != categoryId) {
            categoryBean.eq("category_id", categoryId);
        }
        List<Subject> list = subjectService.list(categoryBean);
        return Result.getSuccess(list);
    }
    @RequestMapping(value = "/paper", method = RequestMethod.POST)
    public Result queryPaperList(@Valid @NotBlank Long subjectId){
        QueryWrapper<Paper> categoryBean =  new QueryWrapper();
        categoryBean.eq("status", 1);
        if(null != subjectId) {
            categoryBean.eq("subject_id", subjectId);
        }
        List<Paper> list = paperService.list(categoryBean);
        return Result.getSuccess(list);
    }
    @RequestMapping(value = "/select/{id}", method = RequestMethod.POST)
    public Result<ExamPaperEditRequestVM> select(@PathVariable Long id) {
        ExamPaperEditRequestVM vm = paperService.examPaperToVM(id);
        return Result.getSuccess(vm);
    }
}
