package com.butorin.testtwitter

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient

class TwitterClient {
    static final String CONSUMER_KEY = 'KSjThyi3fh6XKgozVeR1KUCIq'
    static final String CONSUMER_SECRET = 'rZZTnqPBLmqQVJnJSM204wbH8tzspFAOPDSPXGnH9rEiydL0Zx'
    static final String ACCESS_TOKEN = '894922632969191425-gVfxcruHSXg5bYPjwLqJAjDqDfBHT7y'
    static final String ACCESS_TOKEN_SECRET = 'Up9KIlc2w9LGRCDrlB2Daai36Nj23kg0thQdagYbvvwlE'

    static connect() {
        def twitterClient  = new RESTClient('https://api.twitter.com/1.1/statuses/')
        twitterClient.contentType = ContentType.JSON
        twitterClient.auth.oauth(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET)
        return twitterClient
    }
}
