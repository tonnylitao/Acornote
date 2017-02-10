const Folder = {
    name: 'Folder',
    primaryKey: 'title',
    properties: {
        title: 'string',
        url: {
            type: 'string',
            optional: true
        },
        audioUrl: {
            type: 'string',
            optional: true
        },
        color: 'string',

        playable: {
            type: 'bool',
            default: false
        },
        flipable: {
            type: 'bool',
            default: false
        },
        markable: {
            type: 'bool',
            default: false
        },

        createdAt: 'date',
        updatedAt: 'date',

        items: {
            type: 'list',
            objectType: 'Item'
        }
    }
}

const Item = {
    name: 'Item',
    properties: {
        folder: 'Folder',

        title: 'string',
        des: {
            type: 'string',
            optional: true
        },
        url: {
            type: 'string',
            optional: true
        },
        imgPath: {
            type: 'string',
            optional: true
        },

        createdAt: 'date',
        updatedAt: 'date',

        fliped: {
            type: 'bool',
            default: false
        },
        marked: {
            type: 'bool',
            default: false
        },
    }
}

export default {
    schema: [Folder, Item],
    schemaVersion: 1,
    migration: (oldRealm, newRealm)=>{

    }
}
