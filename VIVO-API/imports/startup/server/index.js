import { Publications } from '../both/api/publications';
import { Visits } from '../both/api/visits';

Meteor.startup(function () {
    Meteor.methods({
        addAdmins: function (users) {
            if (users != undefined)
                _.each(users, function (user) {
                    if (!Roles.userIsInRole(user._id, ['admin']))
                        if (user.username === "admin"
                            || user.username === "Rodrigo Quelhas"
                            || user.username === "André Correia"
                            || user.username === "Rui Pinto"
                            || user.username === "José Machado" )
                            {
                                Roles.addUsersToRoles(user._id, "admin")
                            }
                });
        },
        addVisit: function ( id, user, duration ) {
            return Visits.insert({
                        userId: id,
                        user: user,
                        visitDuration: duration
                   })
        },
        addVisitEvaluation: function ( visitId, evaluation ) {
            Visits.update(
                {'_id': visitId},
                { $set: { visitEvaluation: evaluation }}
            );
        },
        validateUserUpdate: function ( userIdToUpdate, username, email ) {

            userByUsername = Accounts.findUserByUsername(username);
            userByEmail = Accounts.findUserByEmail(email);
            user = Meteor.users.findOne({ _id: userIdToUpdate });

            if (userByUsername)
                if(userByUsername._id !== userIdToUpdate)
                    return false

            if (userByEmail)
                if(userByEmail._id !== userIdToUpdate)
                    return false

            if (!user)
                return false

            if ( username === user.username && email === user.emails[0].address )
                return false

            return true
        }
    });

    JsonRoutes.add("get", "/admins", (req, res, next) => {
        Meteor.call("addAdmins", Accounts.users.find({}).fetch());
    });

    Meteor.publish("publications", function () {
           return Publications.find({}, {fields: { content: 1, createdAt: 1, image: 1 }});
    });

    Meteor.publish("visits", function () {
           return Visits.find({}, {fields: { userId: 1, visitDuration: 1, visitedAt: 1 }});
    });

    Security.permit(['insert', 'update'])
        .collections([Publications, Visits]).ifLoggedIn().allowInClientCode();


    JsonRoutes.setResponseHeaders({
        "Cache-Control": "no-store",
        "Pragma": "no-cache",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS",
        "Access-Control-Allow-Headers": "Content-Type, Authorization, X-Requested-With"
    });

    JsonRoutes.Middleware.use('/publications', (req, res, next) => {
        JsonRoutes.sendResult(res, {
            code: 401
        });
    });

    JsonRoutes.Middleware.use('/', (req, res, next) => {
        Meteor.call("addAdmins", Accounts.users.find({}).fetch());
        next();
        return
    });

    JsonRoutes.add("get", "/user", (req, res, next) => {

        const authUserId = req.userId;

        if (authUserId) {
            JsonRoutes.sendResult(res, {
                code: 200,
                data: Accounts.users.findOne({ _id: authUserId })
            });
            return
        }

        JsonRoutes.sendResult(res, {
            code: 401,
            data: { error: "not allowed" }
        });

    });

    JsonRoutes.add("delete", "/user", (req, res, next) => {
        const authUserId = req.userId;
        const userIdToDelete = req.body.userId;

        if ( authUserId && userIdToDelete )
            if (Roles.userIsInRole(authUserId, ['admin']) && !Roles.userIsInRole(userIdToDelete, ['admin'])){
                Meteor.users.remove( userIdToDelete );
                JsonRoutes.sendResult(res, {
                    code: 200,
                    data: { done: true }
                });
                return
            }

        JsonRoutes.sendResult(res, {
            code: 200,
            data: { done: false }
        });
        return

    });

    JsonRoutes.add("POST", "/user", (req, res, next) => {
        const authUserId = req.userId;
        const userIdToUpdate = req.body.userId;
        const username = req.body.username;
        const email = req.body.email;

        if ( !userIdToUpdate || !username || !email ) {
            JsonRoutes.sendResult(res, {
                code: 401,
                data: { done: false }
            });
            return
        }

        const canUpdate = Meteor.call("validateUserUpdate", userIdToUpdate, username, email);

        if ( authUserId && canUpdate)
            if (Roles.userIsInRole(authUserId, ['admin']) || authUserId === userIdToUpdate){

                Meteor.users.update(userIdToUpdate, {
                    $set: { username: username, 'emails.0.address': email }
                });

                JsonRoutes.sendResult(res, {
                    code: 200,
                    data: { done: true }
                });
                return
            }

        JsonRoutes.sendResult(res, {
            code: 200,
            data: { done: false }
        });
        return
    });

    JsonRoutes.add("get", "/users", (req, res, next) => {

        const authUserId = req.userId;

        if (authUserId) {
            if (Roles.userIsInRole(authUserId, ['admin'])) {
                JsonRoutes.sendResult(res, {
                    code: 200,
                    data: Accounts.users.find({}).fetch()
                });
                return
            } else {
                JsonRoutes.sendResult(res, {
                    code: 200,
                    data: Accounts.users.findOne({ _id: authUserId })
                });
                return
            }
        }

        JsonRoutes.sendResult(res, {
            code: 401,
            data: { error: "not allowed" }
        });

    });
    JsonRoutes.add("post", "/visits", (req, res, next) => {
        const authUserId = req.userId;
        const userId = req.body.userId;

        if (authUserId && userId) {
            if (Roles.userIsInRole(authUserId, ['admin'])) {
                let visits = Visits.find({userId: userId}, {sort: {visitedAt: -1}}).fetch();
                if ( visits.length === 0 ) {
                    JsonRoutes.sendResult(res, {
                        code: 401
                    });
                    return;
                }
                JsonRoutes.sendResult(res, {
                    data: visits
                });
            }
            return;
        }
        sonRoutes.sendResult(res, { code: 401 });
    });

    JsonRoutes.add("get", "/visits", (req, res, next) => {
        const authUserId = req.userId;

        if (authUserId) {
            if (Roles.userIsInRole(authUserId, ['admin'])) {
                let visits = Visits.find({}, {sort: {visitedAt: -1}}).fetch();
                if ( visits.length === 0 ) {
                    JsonRoutes.sendResult(res, {
                        code: 401
                    });
                    return;
                }
                JsonRoutes.sendResult(res, {
                    data: visits
                });
            }
            else {
                let visits = Visits.find({userId: authUserId}, {sort: {visitedAt: -1}}).fetch();
                if ( visits.length === 0 ) {
                    JsonRoutes.sendResult(res, {
                        code: 401
                    });
                    return;
                }
                JsonRoutes.sendResult(res, {
                    data: visits
                });
            }
        } else
            JsonRoutes.sendResult(res, { code: 401 });
    });

    JsonRoutes.add("post", "/newvisit", (req, res, next) => {
        const authUserId = req.userId;

        if (authUserId) {
            let user = Meteor.users.findOne({ _id: authUserId }).username;
            if (req.body.duration != undefined) {
                let visitId = Meteor.call("addVisit", authUserId, user, req.body.duration)
                JsonRoutes.sendResult(res, {
                    data: { visitId: visitId }
                });
            } else {
                JsonRoutes.sendResult(res, {
                    code: 401
                });
            }
        } else {
            JsonRoutes.sendResult(res, {
                code: 401
            });
        }

    });

    JsonRoutes.add("post", "/visiteval", (req, res, next) => {
        const authUserId = req.userId;
        const visitId = req.body.visitId;
        const visit = Visits.findOne({ _id: visitId })

        if (visit.visitEvaluation) {
            JsonRoutes.sendResult(res, {
                code: 401,
                data: { error: "Evaluation already stored!"}
            });
            return
        }

        if ( authUserId !== visit.userId || !authUserId ) {
            JsonRoutes.sendResult(res, {
                code: 401,
                data: { error: "Not authorized!"}
            });
            return
        }

        if ( !req.body.questions || !req.body.answers ) {
            JsonRoutes.sendResult(res, {
                code: 401,
                data: { error: "Params missing!"}
            });
            return;
        }

        let questions = req.body.questions.split(",");
        let answers = req.body.answers.split(",");

        if (questions.length !== questions.length || questions.length < 1) {
            JsonRoutes.sendResult(res, {
                code: 401,
                data: { error: "Params missing!"}
            });
            return;
        }

        let evaluation = []
        for (i = 0; i < questions.length; i++) {
            evaluation.push({
                question: questions[i],
                answer: answers[i]
            })
        }

        Meteor.call("addVisitEvaluation", visitId, evaluation)
        JsonRoutes.sendResult(res, {
            code: 200,
            data: { done: true }
        });
    });

    JsonRoutes.add("get", "/notifications", (req, res, next) => {

        let date_end = new Date()
        let date_start = new Date();
        date_start.setHours(date_end.getHours()-8)

        JsonRoutes.sendResult(res, {
            data: Publications.findOne({
                    createdAt: {
                        $gte: date_start,
                        $lt: date_end
                    }}, {sort: {createdAt: -1}})
        });
    });

    JsonRoutes.add("post", "/newnotification", (req, res, next) => {

        const authUserId = req.userId;

        if (authUserId)
            if (Roles.userIsInRole(authUserId, ['admin']))
                if (req.body.content != undefined) {
                    Publications.insert({content: req.body.content, image: "abc.png"})
                    JsonRoutes.sendResult(res, {
                        data: { done: true }
                    });
                    return;
                }

        JsonRoutes.sendResult(res, {
            code: 401
        });

    });
});
