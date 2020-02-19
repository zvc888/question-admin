package com.question.admin.domain.entity.exam.question;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionObject implements Serializable{

    private String titleContent;

    private String analyze;

    private List<QuestionItemObject> questionItemObjects;

    private String correct;
}
