<template>
  <el-container class="chat-page">
    <el-aside width="260px" class="chat-list-box">
      <div class="chat-list-header">
        <el-input class="search-text" size="small" placeholder="搜索" v-model="searchText">
          <i class="el-icon-search el-input__icon" slot="prefix"></i>
        </el-input>
      </div>
      <div class="chat-list-loading" v-if="loading">
        <div class="chat-loading-box">
          <i class="el-icon-loading"></i>
          <span class="el-loading-text">消息接收中...</span>
        </div>
      </div>
      <el-scrollbar class="chat-list-items" v-else>
        <div v-for="(chat, index) in chatStore.chats" :key="index">
          <chat-item v-show="!chat.delete && chat.showName.includes(searchText)" :chat="chat" :index="index"
            @click.native="onActiveItem(index)" @delete="onDelItem(index)" @top="onTop(index)"
            :active="chat === chatStore.activeChat"></chat-item>
        </div>
      </el-scrollbar>
    </el-aside>
    <el-container class="chat-box">
      <chat-box v-if="chatStore.activeChat" :chat="chatStore.activeChat"></chat-box>
    </el-container>
  </el-container>
</template>

<script>
import ChatItem from "../components/chat/ChatItem.vue";
import ChatBox from "../components/chat/ChatBox.vue";

export default {
  name: "chat",
  components: {
    ChatItem,
    ChatBox
  },
  data() {
    return {
      searchText: "",
      messageContent: "",
      group: {},
      groupMembers: []
    }
  },
  methods: {
    onActiveItem(index) {
      this.$store.commit("activeChat", index);
    },
    onDelItem(index) {
      this.$store.commit("removeChat", index);
    },
    onTop(chatIdx) {
      this.$store.commit("moveTop", chatIdx);
    },
  },
  computed: {
    chatStore() {
      return this.$store.state.chatStore;
    },
    loading() {
      return this.chatStore.loadingGroupMsg || this.chatStore.loadingPrivateMsg
    }
  }
}
</script>

<style lang="scss">
.chat-page {
  display: flex;
  height: 100%;

  .chat-list-box {
    display: flex;
    flex-direction: column;
    background: var(--im-background);

    .chat-list-header {
      height: 50px;
      display: flex;
      align-items: center;
      padding: 0 8px;

      .search-input {
        position: relative;
        width: 100%;
      }
      .search-input .el-input__icon {
        position: absolute;
        left: 8px;
        top: 50%;
        transform: translateY(-50%);
        color: #909399;
        font-size: 14px;
      }
      .search-input .search-text {
        width: 100%;
        height: 32px;
        padding: 0 10px 0 28px;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
        outline: none;
        font-size: 14px;
        color: #606266;
        transition: border-color .2s ease, box-shadow .2s ease;
      }
      .search-input .search-text:focus {
        border-color: var(--im-color-primary);
        box-shadow: 0 0 0 2px rgba(64, 158, 255, .12);
      }
    }

    .chat-list-loading {
      height: 50px;
      background-color: #eee;
      display: flex;
      align-items: center;
      padding: 0 12px;

      .el-icon-loading {
        font-size: 16px;
        color: var(--im-text-color-light);
        margin-right: 8px;
      }

      .el-loading-text {
        color: var(--im-text-color-light);
      }

      .chat-loading-box {
        height: 100%;
        display: flex;
        align-items: center;
      }
    }

    .chat-list-items {
      flex: 1;
      overflow-y: auto;
    }
  }
}
</style>
