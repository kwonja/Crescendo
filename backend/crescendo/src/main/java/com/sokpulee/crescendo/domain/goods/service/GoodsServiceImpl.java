package com.sokpulee.crescendo.domain.goods.service;

import com.sokpulee.crescendo.domain.fanart.entity.FanArt;
import com.sokpulee.crescendo.domain.fanart.entity.FanArtComment;
import com.sokpulee.crescendo.domain.fanart.entity.FanArtLike;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsCommentAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsCommentUpdateRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsUpdateRequest;
import com.sokpulee.crescendo.domain.goods.dto.response.FavoriteGoodsResponse;
import com.sokpulee.crescendo.domain.goods.dto.response.MyGoodsResponse;
import com.sokpulee.crescendo.domain.goods.entity.Goods;
import com.sokpulee.crescendo.domain.goods.entity.GoodsComment;
import com.sokpulee.crescendo.domain.goods.entity.GoodsImage;
import com.sokpulee.crescendo.domain.goods.entity.GoodsLike;
import com.sokpulee.crescendo.domain.goods.repository.GoodsCommentRepository;
import com.sokpulee.crescendo.domain.goods.repository.GoodsLikeRepository;
import com.sokpulee.crescendo.domain.goods.repository.GoodsRepository;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import com.sokpulee.crescendo.domain.idol.repository.IdolGroupRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.*;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsServiceImpl implements GoodsService {
    private final UserRepository userRepository;

    private final IdolGroupRepository idolGroupRepository;

    private final FileSaveHelper fileSaveHelper;

    private final GoodsRepository goodsRepository;

    private final GoodsCommentRepository goodsCommentRepository;

    private final GoodsLikeRepository goodsLikeRepository;


    @Override
    public void addGoods(Long loggedInUserId, GoodsAddRequest goodsAddRequest) {
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        IdolGroup idolGroup = idolGroupRepository.findById(goodsAddRequest.getIdolGroupId())
                .orElseThrow(IdolGroupNotFoundException::new);

        Goods goods = Goods.builder()
                .idolGroup(idolGroup)
                .user(user)
                .title(goodsAddRequest.getTitle())
                .content(goodsAddRequest.getContent())
                .likeCnt(0)
                .commentCnt(0)
                .build();

        if (!goodsAddRequest.getImageList().isEmpty()) {
            for (MultipartFile goodsImageFile : goodsAddRequest.getImageList()) {
                String savePath = fileSaveHelper.saveGoodsImage(goodsImageFile);

                GoodsImage goodsImage = GoodsImage.builder()
                        .imagePath(savePath)
                        .build();
                System.out.println(goodsImage.toString());

                goods.addImage(goodsImage);
            }
        }

        goodsRepository.save(goods);
    }

    @Override
    public void deleteGoods(Long goodsId, Long loggedInUserId) {
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(GoodsNotFoundException::new);

        if (!goods.getUser().getId().equals(loggedInUserId)) {
            throw new UnAuthorizedAccessException();
        }

        if (goodsRepository.existsById(goodsId)) {
            goodsRepository.deleteById(goodsId);
        } else {
            throw new GoodsNotFoundException();
        }
    }

    @Override
    public void updateGoods(Long loggedInUserId, Long goodsId, GoodsUpdateRequest goodsUpdateRequest) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(GoodsNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        IdolGroup idolGroup = idolGroupRepository.findById(goodsUpdateRequest.getIdolGroupId())
                .orElseThrow(IdolGroupNotFoundException::new);

        if (!goods.getUser().getId().equals(loggedInUserId)) {
            throw new UnAuthorizedAccessException();
        }

        goods.changeGoods(idolGroup, goodsUpdateRequest.getTitle(), goodsUpdateRequest.getContent());

        goods.getImageList().clear();

        if (!goodsUpdateRequest.getImageList().isEmpty()) {
            for (MultipartFile goodsImageFile : goodsUpdateRequest.getImageList()) {
                String savePath = fileSaveHelper.saveGoodsImage(goodsImageFile);

                GoodsImage goodsImage = GoodsImage.builder()
                        .imagePath(savePath)
                        .build();

                goods.addImage(goodsImage);
            }
        }
        goodsRepository.save(goods);
    }

    @Override
    public void deleteGoodsComment(Long loggedInUserId, Long goodsId, Long goodsCommentId) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(GoodsNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        GoodsComment goodsComment = goodsCommentRepository.findById(goodsCommentId)
                .orElseThrow(GoodsCommentNotFoundException::new);

        if (!goodsComment.getUser().getId().equals(loggedInUserId)) {
            throw new UnAuthorizedAccessException();
        }

        if (goodsComment.getParentGoodsComment() != null) {
            goodsComment.getParentGoodsComment().minusReplyCnt();
        }

        goods.minusCommentCnt(goodsComment.getReplyCnt());


        goodsCommentRepository.delete(goodsComment);
    }

    @Override
    public void updateGoodsComment(Long loggedInUserId, Long goodsId, Long goodsCommentId, GoodsCommentUpdateRequest goodsCommentUpdateRequest) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(GoodsNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        GoodsComment goodsComment = goodsCommentRepository.findById(goodsCommentId)
                .orElseThrow(GoodsCommentNotFoundException::new);

        if (!goodsComment.getUser().getId().equals(loggedInUserId)) {
            throw new UnAuthorizedAccessException();
        }

        goodsComment.changeComment(goodsCommentUpdateRequest.getContent());

        goodsCommentRepository.save(goodsComment);
    }

    @Override
    public void likeGoods(Long loggedInUserId, Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(GoodsNotFoundException::new);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Optional<GoodsLike> existingGoodsLike = goodsLikeRepository.findByGoodsAndUser(goods,user);

        if(existingGoodsLike.isPresent()){
            goodsLikeRepository.delete(existingGoodsLike.get());
            goods.minusLikeCnt();
        }else{
            GoodsLike goodsLike = GoodsLike.builder()
                    .user(user)
                    .goods(goods)
                    .build();
            goods.plusLikeCnt();
            goodsLikeRepository.save(goodsLike);
        }
    }

    @Override
    public Page<FavoriteGoodsResponse> getFavoriteGoods(Long loggedInUserId, Pageable pageable) {
        return goodsRepository.findFavoriteGoods(loggedInUserId, pageable);
    }

    @Override
    public Page<MyGoodsResponse> getMyGoods(Long loggedInUserId, Pageable pageable) {
        return goodsRepository.findMyGoods(loggedInUserId,pageable);
    }

    @Override
    public void addGoodsComment(Long loggedInUserId, Long goodsId, GoodsCommentAddRequest goodsCommentAddRequest) {
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(GoodsNotFoundException::new);

        GoodsComment goodsComment = GoodsComment.builder()
                .goods(goods)
                .user(user)
                .content(goodsCommentAddRequest.getContent())
                .build();

        goods.plusCommentCnt();

        goodsCommentRepository.save(goodsComment);
    }

    @Override
    public void addGoodsReply(Long loggedInUserId, Long goodsId, Long goodsCommentId, GoodsCommentAddRequest goodsReplyAddRequest) {
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(GoodsNotFoundException::new);

        GoodsComment parentGoodsComment = goodsCommentRepository.findById(goodsCommentId)
                .orElseThrow(GoodsCommentNotFoundException::new);

        if (parentGoodsComment.getGoods().getGoodsId() == goodsId) {
            GoodsComment goodsComment = GoodsComment.builder()
                    .goods(goods)
                    .parentGoodsComment(parentGoodsComment)
                    .user(user)
                    .content(goodsReplyAddRequest.getContent())
                    .build();

            if (parentGoodsComment.getParentGoodsComment() == null) {
                goodsCommentRepository.save(goodsComment);
                goods.plusCommentCnt();
                parentGoodsComment.plusReplyCnt();
            } else {
                throw new FanArtCommentNotFoundException();
            }
        } else {
            throw new FanArtCommentNotFoundException();
        }
    }


}
