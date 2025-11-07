import { defineStore } from 'pinia';
import { registerMember, getMe, patchMember, toHotboardVisible, toProfileVisibility, logoutMember, withdrawMember } from '@/entities';

export const useMeStore = defineStore('member', {
    state: () => ({
        id: null,

        // 사용자 정보
        email: null,
        name: null,
        birthDate: null,

        // 프로필
        nickname: null,
        avatarImage: null,
        backgroundImage: null,
        hotboardVisible: null,
    }),

    getters: {
        isAuthenticated: (state) => state.id !== null,
        isRegistered: (state) => !!(state.name && state.birthDate),
    },

    actions: {
        async getMyId() {
            if (this.id == null) {
                await this.fetchMe();
            }

            return this.id;
        },

        async fetchMe() {
            const data = await getMe();

            this.id = data.id;
            this.avatarImage = data.avatarImage;
            this.backgroundImage = data.backgroundImage;
            this.email = data.email;
            this.name = data.name;
            this.nickname = data.nickname;
            this.birthDate = data.birthDate;
            this.hotboardVisible = toHotboardVisible(data.profileVisibility);
        },

        async register(name, birthDate) {
            const response = await registerMember(name, birthDate);

            this.name = response.name;
            this.birthDate = response.birthDate;

            return response;
        },

        async tryEditMember(editedInfo) {
            const formData = new FormData();

            if (editedInfo.nickname !== this.nickname) {
                formData.append('nickname', editedInfo.nickname);
            }
            if (editedInfo.birthDate !== this.birthDate) {
                formData.append('birthDate', editedInfo.birthDate);
            }
            if (editedInfo.hotboardVisible !== this.hotboardVisible) {
                formData.append('profileVisibility', toProfileVisibility(editedInfo.hotboardVisible));
            }

            if (editedInfo.avatarImage?.startsWith('data:image/')) {
                const avatarBlob = await fetch(editedInfo.avatarImage).then(res => res.blob());
                formData.append('avatarImage', avatarBlob, 'avatar.png');
            }
            if (editedInfo.backgroundImage?.startsWith('data:image/')) {
                const backgroundBlob = await fetch(editedInfo.backgroundImage).then(res => res.blob());
                formData.append('backgroundImage', backgroundBlob, 'background.png');
            }

            if (editedInfo.removeAvatarImage) {
                formData.append('removeAvatarImage', 'true');
            }
            if (editedInfo.removeBackgroundImage) {
                formData.append('removeBackgroundImage', 'true');
            }

            await patchMember(formData);
        },


        async logout() {
            try {
                await logoutMember();
            } finally {
                this.clearAuth();
            }
        },

        async withdraw() {
          try {
            await withdrawMember();

            this.clearAuth();
          } catch (error) {
            console.error("회원 탈퇴 실패:", error);
            throw error;
          }
        },

        clearAuth() {
          this.$reset();
        }
    },
  },
);
