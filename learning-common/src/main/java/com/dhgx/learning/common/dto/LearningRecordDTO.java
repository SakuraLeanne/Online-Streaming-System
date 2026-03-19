package com.dhgx.learning.common.dto;

import com.dhgx.learning.common.enums.LearningRecordStatus;

import java.io.Serializable;

/**
 * 学习记录 DTO。
 */
public class LearningRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long courseId;
    private Long lessonId;
    private Integer progress;
    private LearningRecordStatus status;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public LearningRecordStatus getStatus() {
        return status;
    }

    public void setStatus(LearningRecordStatus status) {
        this.status = status;
    }
}
