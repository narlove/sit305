const { Long } = require('mongodb');

function generate_id()
{
    // generate a unique number id that is based off of the date with a small random mod
    // to ensure that if an id is generated twice in one millisecond that it should be the unique

    // long because we will only use it in a long because our mongodb id is required to be long
    // to work with conversion over from old db
    return Long.fromNumber(Date.now() + Math.floor(Math.random() * 1000));
}

module.exports = { generate_id };