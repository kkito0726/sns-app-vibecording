package com.example.flick.infra.user

import com.example.flick.domain.follow.Follow
import com.example.flick.domain.follow.FollowRepository
import com.example.flick.gen.jooq.Tables.FOLLOWS
import com.example.flick.gen.jooq.Tables.USERS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class JooqFollowRepository(
    private val dslContext: DSLContext
) : FollowRepository {

    override fun follow(followerId: Long, followingId: Long) {
        dslContext.insertInto(FOLLOWS)
            .set(FOLLOWS.FOLLOWER_ID, followerId)
            .set(FOLLOWS.FOLLOWING_ID, followingId)
            .execute()
    }

    override fun unfollow(followerId: Long, followingId: Long) {
        dslContext.deleteFrom(FOLLOWS)
            .where(FOLLOWS.FOLLOWER_ID.eq(followerId))
            .and(FOLLOWS.FOLLOWING_ID.eq(followingId))
            .execute()
    }

    override fun isFollowing(followerId: Long, followingId: Long): Boolean {
        return dslContext.selectCount()
            .from(FOLLOWS)
            .where(FOLLOWS.FOLLOWER_ID.eq(followerId))
            .and(FOLLOWS.FOLLOWING_ID.eq(followingId))
            .fetchOne(0, Long::class.java)!! > 0
    }

    override fun getFollowerCount(userId: Long): Long {
        return dslContext.selectCount()
            .from(FOLLOWS)
            .where(FOLLOWS.FOLLOWING_ID.eq(userId))
            .fetchOne(0, Long::class.java)!!
    }

    override fun getFollowingCount(userId: Long): Long {
        return dslContext.selectCount()
            .from(FOLLOWS)
            .where(FOLLOWS.FOLLOWER_ID.eq(userId))
            .fetchOne(0, Long::class.java)!!
    }
}