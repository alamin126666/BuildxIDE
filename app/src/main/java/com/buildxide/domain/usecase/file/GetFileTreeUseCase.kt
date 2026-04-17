package com.buildxide.domain.usecase.file

import com.buildxide.data.repository.FileRepository
import com.buildxide.domain.model.FileNode
import com.buildxide.domain.model.FileType
import javax.inject.Inject

class GetFileTreeUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(
        owner: String,
        repo: String,
        path: String = "",
        branch: String? = null
    ): Result<List<FileNode>> {
        return fileRepository.getFolderContent(owner, repo, path, branch).map { items ->
            items.map { item ->
                FileNode(
                    name = item.name,
                    path = item.path,
                    isDirectory = item.type == "dir",
                    sha = item.sha,
                    size = item.size
                )
            }.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
        }
    }
}
