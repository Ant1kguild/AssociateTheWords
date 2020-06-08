package com.words.association.domain

import com.words.association.data.datasource.firebase.model.User
import io.reactivex.Completable

class UpdateUserUseCase(private val userRepository: UserRepository) :
    BaseCompletableUseCaseWithParam<User> {

    override fun execute(param: User): Completable {
        return userRepository.updateUser(user = param)
    }
}