/**
 * Created by pradyumnad on 11/15/15.
 */
angular.module("app", [])

    .controller("HomeCtrl", function ($scope, $http) {

        $scope.trends = [];
        $http.get("https://api.mongolab.com/api/1/databases/cs5543/collections/TwitterSentiment?apiKey=jwTtZxj0UFvt_JDGFsAcPUfPT_jBY_JA&l=10")
            .then(
            function (response) {
                console.log(response);
                $scope.trends = response.data;
            }, function (err) {
                console.log(err);
            });

    });