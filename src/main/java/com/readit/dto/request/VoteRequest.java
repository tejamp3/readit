package com.readit.dto.request;

import com.readit.entity.Vote.VoteType;
import jakarta.validation.constraints.NotNull;

public class VoteRequest {

    @NotNull
    private VoteType voteType;

    public VoteRequest() {}

    public VoteType getVoteType() { return voteType; }
    public void setVoteType(VoteType voteType) { this.voteType = voteType; }
}