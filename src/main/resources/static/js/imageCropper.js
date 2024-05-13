 // imageCropper.js
class ImageCropper {
  constructor(modalId, imageElementId, resultCallback) {
    // 容器元素的ID，用於裁剪的圖片元素
    this.modalId = modalId;
    this.imageElementId = imageElementId;
    this.resultCallback = resultCallback; // 裁剪完成後的回調函數
    this.cropper = null; // Cropper實例
    this.initModal();
  }

  // 初始化模態框和相關事件綁定
  initModal() {
    const modalElement = document.getElementById(this.modalId);
    const imageElement = document.getElementById(this.imageElementId);

    // 檢查元素是否存在
    if (!modalElement || !imageElement) {
      console.error('Modal or image element not found');
      return;
    }

    // 初始化Cropper
    this.cropper = new Cropper(imageElement, {
      aspectRatio: 1,
      viewMode: 1,
      background: false,
      autoCropArea: 1,
      cropBoxResizable: false,
    });

    // 綁定確認和取消按鈕事件
    modalElement.querySelector('.confirm-crop').addEventListener('click', () => this.confirmCrop());
    modalElement.querySelector('.cancel-crop').addEventListener('click', () => this.cancelCrop());
  }

  // 打開模態框進行圖片裁剪
  openCropper(file) {
    if (!file) {
      console.error('No file provided');
      return;
    }

    const reader = new FileReader();
    reader.onload = (e) => {
      this.cropper.replace(e.target.result);
      $(`#${this.modalId}`).modal('show');
    };
    reader.readAsDataURL(file);
  }

  // 確認剪裁
  confirmCrop() {
    if (this.cropper) {
      const croppedCanvas = this.cropper.getCroppedCanvas();
      const croppedImageUrl = croppedCanvas.toDataURL();
      this.resultCallback(croppedImageUrl);
      $(`#${this.modalId}`).modal('hide');
    }
  }

  // 取消剪裁
  cancelCrop() {
    if (this.cropper) {
      $(`#${this.modalId}`).modal('hide');
    }
  }
}

export default ImageCropper;
