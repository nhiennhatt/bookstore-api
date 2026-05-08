package com.nhiennhatt.bookstoreapi.services;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.common.enums.UserRole;
import com.nhiennhatt.bookstoreapi.models.BookCollection;
import com.nhiennhatt.bookstoreapi.repository.CollectionRepository;
import com.nhiennhatt.bookstoreapi.validations.bookCollection.CreateBookCollectionValidation;
import com.nhiennhatt.bookstoreapi.validations.bookCollection.QueryBookCollectionValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BookCollectionService {
    @Autowired
    private CollectionRepository collectionRepository;

    @Transactional
    public BookCollection createBookCollection(CreateBookCollectionValidation inform) {
        long count = collectionRepository.count();

        BookCollection bookCollection = new BookCollection();
        bookCollection.setName(inform.getName());
        bookCollection.setPriority((int) (count + 1));
        bookCollection.setPublic(inform.isPublic());
        return collectionRepository.save(bookCollection);
    }

    public List<BookCollection> getAllBookCollections(QueryBookCollectionValidation query, CurrentUser user) {
        Boolean isPublic = null;
        if (user == null || user.getRole() == UserRole.ROLE_CUSTOMER) {
            isPublic = true;
        } else {
            isPublic = query.getIsPublic();
        }

        return collectionRepository.paginate(
                query.getPage(),
                query.getLimit(),
                isPublic,
                query.getKeyword()
        );
    }

    @Transactional
    public void updateCollectionPriority(UUID id, int priority) {
        collectionRepository.updatePriority(id, priority);
    }

    @Transactional
    public void deleteCollection(UUID id) {
        collectionRepository.deleteCollection(id);
    }

    @Transactional
    public int updateCollection(UUID id, String name, boolean isPublic) {
        return collectionRepository.updateCollection(id, name, isPublic);
    }
}
