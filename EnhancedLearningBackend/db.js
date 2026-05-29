const { MongoClient, ServerApiVersion } = require("mongodb");

// borrowed from mongo website
const client = new MongoClient(process.env.MONGODB_URI, {
    serverApi: {
        version: ServerApiVersion.v1,
        strict: true,
        deprecationErrors: true,
    }
});

// forward dec
let db;

const connectDb = async () => {
    await client.connect();
    db = client.db(process.env.DB_NAME || "appdatabase");
    console.log("connected!");
};

const getDb = () => {
    if (!db) throw new Error("database not initialised; call connectDb first");
    return db;
};

process.on('SIGINT', async () => {
    await client.close();
    console.log('closed!');
    process.exit(0);
});

module.exports = { connectDb, getDb };