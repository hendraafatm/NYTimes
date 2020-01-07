package com.app.nytimes.helper;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public final class ErrorResponse extends AppResponse {
    @SerializedName("status")
    @Nullable
    private Integer status;

    @Nullable
    public final Integer getStatus() {
        return this.status;
    }

    public final void setStatus(@Nullable Integer var1) {
        this.status = var1;
    }

    public ErrorResponse(@Nullable Integer status) {
        this.status = status;
    }

}