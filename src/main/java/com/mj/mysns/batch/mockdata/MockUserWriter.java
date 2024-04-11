package com.mj.mysns.batch.mockdata;

import com.mj.mysns.user.entity.User;
import com.mj.mysns.user.repository.UserRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@RequiredArgsConstructor
public class MockUserWriter implements ItemWriter<User> {

    private final UserRepository userRepository;

    @Override
    public void write(Chunk<? extends User> chunk) throws Exception {
        User user = chunk.getItems().get(0);

        Random random = new Random();
        while (userRepository.findByUsername(user.getUsername()).isPresent()) {
            user.setUsername(user.getUsername() + random.nextInt(100));
        }

        userRepository.saveAll(chunk);
    }
}
