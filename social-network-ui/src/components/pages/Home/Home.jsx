import { Box } from '@mui/material';
import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { getPost } from '../../../features/slices/homeSlice';
import ButtonShowMore from '../../ButtonShowMore/ButtonShowMore';
import Following from '../../Following/Following';
import HeaderMain from '../../HeaderMain/HeaderMain';
import ModalUser from '../../ModalUser/ModalUser';
import PostList from '../../PostList/PostList';

function Home() {
  const recommendation = useSelector((state) => state.home.recommendation);
  const following = useSelector((state) => state.home.following);
  const modalUserState = useSelector((state) => state.home.modalUser);
  const dispatch = useDispatch();

  const fetchPost = () => {
    fetch('./data.json')
      .then((r) => r.json())
      .then((products) => {
        dispatch(getPost(products));
      })
      .catch((error) => {
        alert(error);
      });
  };

  useEffect(() => {
    fetchPost();
  }, []);

  return (
    <Box
      sx={{
        width: '33%',
        backgroundColor: ' #1e2028',
        display: 'grid',
        marginLeft: '33%',
        paddingTop: '114px',
        paddingBottom: '20px',
      }}
    >
      <HeaderMain />
      <ButtonShowMore />
      {recommendation && <PostList />}
      {following && <Following />}
      {modalUserState && <ModalUser />}
    </Box>
  );
}

export default Home;
