package com.example.instaclone.models

class Reels {
    var reelUrl:String = ""
    var caption:String = ""
    var profile_link:String? = null
    constructor()

    constructor(reelUrl: String, caption: String) {
        this.reelUrl = reelUrl
        this.caption = caption
    }

    constructor(reelUrl: String, caption: String, profile_link: String) {
        this.reelUrl = reelUrl
        this.caption = caption
        this.profile_link = profile_link
    }

}