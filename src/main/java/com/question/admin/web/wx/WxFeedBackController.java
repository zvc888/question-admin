package com.question.admin.web.wx;


import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.domain.entity.exam.FeedBack;
import com.question.admin.domain.vo.Result;
import com.question.admin.service.exam.FeedBackService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping(value = "/api/wx/feedback")
@AllArgsConstructor
public class WxFeedBackController {

    private final FeedBackService feedBackService;
    /**
     * 保存
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public Result save(@RequestBody @Valid FeedBack model) {
        model.setStatus(1);
        Date now = Date.from(Instant.now());
        model.setModifiedTime(now);
        model.setCreatedTime(now);
        Long userId = UserContext.getCurrentUser().getUserId();
        model.setCreatorId(userId);
        model.setEditorId(userId);
        boolean save = feedBackService.save(model);
        return Result.getSuccess(save);
    }
}
