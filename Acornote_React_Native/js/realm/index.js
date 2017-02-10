import Realm from 'realm';
import schemas from './schema';

const realm = new Realm(schemas)

if (realm.objects('Folder').length < 10) {
    realm.write(() => {
        for (let i=0; i<10; i++) {

            realm.create('Folder', {
                title: 'Vocabulary'+Math.random()*1000,
                color: 'blue',
                createdAt: new Date(),
                updatedAt: new Date(),
                items: [{
                    title: 'Hello',
                    des: 'world',
                    createdAt: new Date(),
                    updatedAt: new Date(),
                }]
            })
        }
    })
}

export default realm
