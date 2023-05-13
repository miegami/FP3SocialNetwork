package com.socialnetwork.api.models.additional;

import com.socialnetwork.api.models.additional.keys.RetweetPk;
import com.socialnetwork.api.models.base.Post;
import com.socialnetwork.api.models.base.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "retweets")
public class Retweet {
  @EmbeddedId
  private RetweetPk retweetPk;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User retweetedBy;

  @ManyToOne
  @MapsId("postId")
  @JoinColumn(name = "post_id")
  private Post retweetedPost;
}
