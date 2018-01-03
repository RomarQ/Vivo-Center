import SimpleSchema from 'simpl-schema';

// Creates a Collection to store publications
export const Visits = new Mongo.Collection("visits");

const Schemas = {};

Schemas.Visit = new SimpleSchema ({
    userId: {
        type: String
    },
    user: {
        type: String
    },
    visitDuration: {
        type: String
    },
    // Stores the date of when the message was sent
    visitedAt: {
        type: Date,
        denyUpdate: true,
        autoValue: function() {
            if (this.isInsert)
                return new Date();
        }
    },
    visitEvaluation: {
        type: Array,
        optional: true
    },
    'visitEvaluation.$': {
        type: Object
    },
    'visitEvaluation.$.question': {
        type: String
    },
    'visitEvaluation.$.answer': {
        type: Number
    }
});

Visits.attachSchema(Schemas.Visit);
