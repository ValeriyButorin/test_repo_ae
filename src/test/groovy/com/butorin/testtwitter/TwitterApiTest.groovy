package com.butorin.testtwitter
import spock.lang.Shared
import spock.lang.Specification



class TwitterApiTest extends Specification {

    @Shared twitterApi

    def setupSpec(){
        twitterApi = new TwitterClient().connect()
    }

    def "test-statuses-home-timeline"() {
        given: "End point by the following link https://dev.twitter.com/rest/reference/get/statuses/home_timeline"
        def homeTimeLineResp = twitterApi.get( path: 'home_timeline.json')
        expect: "Valid response"
        assert homeTimeLineResp.status == 200: "Response code should be 200"
        homeTimeLineResp.data.each{assert it.created_at != null:"created_at should be null"}
        homeTimeLineResp.data.each{assert it.retweet_count >= 0:"retweet_count should be greater than 0"}
        homeTimeLineResp.data.each{assert it.text != "":"text should be empty sting"}
    }

    def "test-statuses-destroy"() {
        given: "Check ability to delete tweet according to https://dev.twitter.com/rest/reference/post/statuses/destroy/id"
        and: "create tweet to delete"
        def createResponse = twitterApi.post(
                path: 'update.json',
                query : [status: "Test data to Delete${System.currentTimeSeconds()}"],
        )
        def tweetId = createResponse.responseData.id_str

        and: "Delete created tweet"
        def deleteResp = twitterApi.post(path: "destroy/${tweetId}.json")


        expect:"Repeat request to delete should return response code 404"
        try{
            deleteResp2 = twitterApi.post(path: "destroy/${tweetId}.json")
        }catch(Exception e){
            assert e.statusCode == 404: "Expected Response code for 2nd attempt to delete tweet is 404"
        }
    }
    def "test-statuses-update"() {
        given : "End point by the following ling https://dev.twitter.com/rest/reference/post/statuses/update"
        and: "Ability to update tweet "
        String expectedStatus ="Test Data: Ability to update tweet ${System.currentTimeSeconds()}"
        def updateResp = twitterApi.post(
                path: 'update.json',
                body: [status: "${expectedStatus}", source: 'testtwitterclient'],
                requestContentType : 'application/x-www-form-urlencoded')
        expect:"Check ability to update tweet, expectation is new status"
        assert updateResp.status == 200: "For update operation response code should be 200"
        assert  updateResp.responseData.text == expectedStatus: "After update status should equal to expected"
        }
    def "test-statuses-update_dublicate"() {
        given : "End point by the following ling https://dev.twitter.com/rest/reference/post/statuses/update"
        and: "Ability to update tweet 2nd time"
        String expectedStatus ="Test Data: Ability to update tweet ${System.currentTimeSeconds()}"
        def updateResp = twitterApi.post(
                path: 'update.json',
                body: [status: "${expectedStatus}", source: 'testtwitterclient'],
                requestContentType : 'application/x-www-form-urlencoded')
        expect:"Check than 2nd attempt to update tweet with the same body return response code 403"
        assert updateResp.status == 200: "For update operation response code should be 200"
                try{
                    updateResp2 = twitterApi.post(
                            path: 'update.json',
                            body: [status: "${expectedStatus}", source: 'testtwitterclient'],
                            requestContentType : 'application/x-www-form-urlencoded')
            }catch(Exception e){
                assert e.statusCode == 403: "Expected Response code for 2nd attempt to update tweet is 403"
            }
    }}