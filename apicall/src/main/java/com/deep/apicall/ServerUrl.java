package com.deep.apicall;

public enum ServerUrl {
    TestList("dr_api/teacher_api/test_paper_list.php"),
    QuestionList("dr_api/teacher_api/question_ans_list.php"),
    UpdateQuestion("dr_api/teacher_api/update_test_paper_question.php"),
    AddQuestion("dr_api/teacher_api/insert_paper_question.php"),
    UpdateTest("dr_api/teacher_api/update_test_paper.php"),
    CreateTest("dr_api/teacher_api/create_test_paper.php"),
    SendTestForReview("dr_api/teacher_api/update_review_status.php"),
    TestAllList("dr_api/approved_test_paper_list.php"),
    CheckTeacherActive("dr_api/teacher_api/check_teacher_login.php"),
    UserTestInstruction("dr_api/teacher_api/test_instruction.php");

    String value;
    ServerUrl(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
