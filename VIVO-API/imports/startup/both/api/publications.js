import SimpleSchema from 'simpl-schema';

// Creates a Collection to store publications
export const Publications = new Mongo.Collection("publications");

const Schemas = {};

Schemas.Publication = new SimpleSchema ({
    content: {
        type: String
    },
    image: {
        type: String
    },
    // Stores the date of when the message was sent
    createdAt: {
        type: Date,
        denyUpdate: true,
        autoValue: function() {
            if (this.isInsert)
                return new Date();
        }
    },
});

Publications.attachSchema(Schemas.Publication);

Publications.publicFields = {
  content: 1,
  image: 1,
  createdAt: 1
};
